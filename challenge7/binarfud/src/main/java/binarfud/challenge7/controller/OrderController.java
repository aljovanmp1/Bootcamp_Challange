package binarfud.challenge7.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

import binarfud.challenge7.dto.order.others.InvoiceDto;
import binarfud.challenge7.dto.order.others.InvoiceDto.OrderPerMerchant;
import binarfud.challenge7.dto.order.others.InvoiceDto.OrderPerMerchant.OrderDetailWithProduct;
import binarfud.challenge7.dto.order.request.AddOrderDto;
import binarfud.challenge7.dto.order.response.AddOrdersRespDto;
import binarfud.challenge7.model.Order;
import binarfud.challenge7.model.OrderDetail;
import binarfud.challenge7.model.Product;
import binarfud.challenge7.model.User;
import binarfud.challenge7.service.OrderDetailService;
import binarfud.challenge7.service.OrderService;
import binarfud.challenge7.service.ProductService;
import binarfud.challenge7.service.InvoiceService;
import binarfud.challenge7.service.UserService;
import binarfud.challenge7.utlis.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("binarfud/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody AddOrderDto dataDto,
            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            List<OrderDetail> orderDetailList = new ArrayList<>();

            for (AddOrderDto.OrderData singleOrder : dataDto.getData()) {
                Optional<Product> productOptional = productService.getProductById(singleOrder.getProductId());
                if (!productOptional.isPresent())
                    throw new DataNotFoundException("Product id: " + singleOrder.getProductId() + " not found");

                OrderDetail newOrder = new OrderDetail();
                newOrder.setNote(singleOrder.getNote());
                newOrder.setProduct(productOptional.get());
                newOrder.setQuantity(singleOrder.getQuantity());
                newOrder.setTotalPrice(productOptional.get().getPrice() * singleOrder.getQuantity());

                orderDetailList.add(newOrder);
            }

            String headerAuthorization = request.getHeader("Authorization");

            User user = userService.getUserByToken(headerAuthorization)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Username not found or Authentication is not valid"));

            Order order = new Order();
            order = orderService.saveOrder(order, dataDto.getDestination(), user.getId());

            var wrapper = new Object() {
                int totalPrice = 0;
            };
            List<OrderDetail> savedOrders = orderDetailService.saveOrderDetails(orderDetailList, order);
            List<AddOrdersRespDto> respDto = savedOrders
                    .stream()
                    .map(orderDetail -> {
                        wrapper.totalPrice += orderDetail.getTotalPrice();
                        return modelMapper.map(orderDetail, AddOrdersRespDto.class);
                    })
                    .collect(Collectors.toList());

            data.put("allProductPrice", wrapper.totalPrice);
            data.put("orders", respDto);
            data.put("destination", order.getDestinationOrder());
            data.put("orderId", order.getId());

            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException | UsernameNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    }

    @GetMapping("invoice")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<?> getUserOrder(@RequestParam UUID id,
            HttpServletRequest request) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String headerAuthorization = request.getHeader("Authorization");
            User user = userService.getUserByToken(headerAuthorization)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Username not found or Authentication is not valid"));

            Optional<Order> selectedOrder = orderService.getOrderById(id);
            if (!selectedOrder.isPresent())
                throw new DataNotFoundException("Order not found");

            Order order = selectedOrder.get();

            if (order.getUserId() != user.getId())
                throw new DataNotFoundException("Users dont have acces to order");

            InvoiceDto invoiceDto = new InvoiceDto();

            invoiceDto.setDest(order.getDestinationOrder());
            invoiceDto.setOrderId(id);
            invoiceDto.setOrderTime(order.getOrderTime().toString());
            invoiceDto.setUserName(user.getUsername());

            List<OrderDetail> orderDetailData = orderDetailService.getOrderDetailsByOrderId(id);

            if (orderDetailData.isEmpty()) {
                invoiceDto.setTotalQty(0);
                invoiceDto.setTotalPrice("0");
                List<OrderPerMerchant> orderPerMerchant = new ArrayList<>();
                invoiceDto.setOrders(orderPerMerchant);
            }

            else {
                int qty = 0;
                Integer totalPrice = 0;
                Map<String, List<OrderDetail>> groupedByMerchant = orderDetailData.stream()
                        .collect(Collectors
                                .groupingBy(orderData -> orderData.getProduct().getMerchant().getMerchantName()));

                List<OrderPerMerchant> orderPerMerchants = new ArrayList<>();
                for (Map.Entry<String, List<OrderDetail>> entry : groupedByMerchant.entrySet()) {
                    OrderPerMerchant orderPerMerchant = new OrderPerMerchant();

                    orderPerMerchant.setMerchantName(entry.getKey());
                    List<OrderDetailWithProduct> orderDetailWithProducts = new ArrayList<>();
                    for (OrderDetail orderDetail : entry.getValue()) {
                        OrderDetailWithProduct orderDetailWithProduct = new OrderDetailWithProduct();

                        orderDetailWithProduct.setProductName(orderDetail.getProduct().getProductName());
                        orderDetailWithProduct.setPrice(orderDetail.getProduct().getPrice().toString());
                        orderDetailWithProduct.setQty(orderDetail.getQuantity());
                        orderDetailWithProduct.setTotal(orderDetail.getTotalPrice().toString());
                        orderDetailWithProducts.add(orderDetailWithProduct);

                        qty += orderDetail.getQuantity();
                        totalPrice += orderDetail.getTotalPrice();
                    }
                    orderPerMerchant.setOrderData(orderDetailWithProducts);
                    orderPerMerchants.add(orderPerMerchant);
                }
                invoiceDto.setOrders(orderPerMerchants);
                invoiceDto.setTotalQty(qty);
                invoiceDto.setTotalPrice(totalPrice.toString());
            }

            ByteArrayOutputStream target = new ByteArrayOutputStream();

            HtmlConverter.convertToPdf(invoiceService.getHtmlStr(invoiceDto), target);

            /* extract output as bytes */
            byte[] bytes = target.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bytes);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("merchant", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    }

}
