package challange_5.binarfud.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

import challange_5.binarfud.model.Merchant;
import challange_5.binarfud.model.Order;
import challange_5.binarfud.model.OrderDetail;
import challange_5.binarfud.model.Product;
import challange_5.binarfud.model.dto.AddOrderDto;
import challange_5.binarfud.model.dto.AddOrdersRespDto;
import challange_5.binarfud.model.dto.AddProductDto;
import challange_5.binarfud.model.dto.AddProductRespDto;
import challange_5.binarfud.model.dto.InvoiceDto;
import challange_5.binarfud.model.dto.InvoiceDto.OrderPerMerchant;
import challange_5.binarfud.model.dto.InvoiceDto.OrderPerMerchant.OrderDetailWithProduct;
import challange_5.binarfud.service.OrderDetailService;
import challange_5.binarfud.service.OrderService;
import challange_5.binarfud.service.ProductService;
import challange_5.binarfud.service.InvoiceService;
import challange_5.binarfud.service.UserService;
import challange_5.binarfud.utlis.DataNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/orders")
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
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody AddOrderDto dataDto) {
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

            Order order = new Order();
            order = orderService.saveOrder(order, dataDto.getDestination());

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

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    }

    @GetMapping("invoice")
    public ResponseEntity<?> getUserOrder(@RequestParam UUID id) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Optional<Order> selectedOrder = orderService.getOrderById(id);
            if (!selectedOrder.isPresent())
                throw new DataNotFoundException("Order not found");

            Order order = selectedOrder.get();
            InvoiceDto invoiceDto = new InvoiceDto();

            invoiceDto.setDest(order.getDestinationOrder());
            invoiceDto.setOrderId(id);
            invoiceDto.setOrderTime(order.getOrderTime().toString());
            invoiceDto.setUserName(order.getUser().getUserName());

            List<OrderDetail> orderDetailData = orderDetailService.getOrderDetailsByOrderId(id);

            if (orderDetailData.isEmpty()){
                invoiceDto.setTotalQty(0);
                invoiceDto.setTotalPrice("0");
                List<OrderPerMerchant> orderPerMerchant = new ArrayList<>();
                invoiceDto.setOrders(orderPerMerchant);
            }

            else {
                int qty = 0;
                Integer totalPrice = 0;
                Map<String, List<OrderDetail>> groupedByMerchant = orderDetailData.stream()
                    .collect(Collectors.groupingBy(orderData -> orderData.getProduct().getMerchant().getMerchantName()));

                    List<OrderPerMerchant> orderPerMerchants = new ArrayList<>();
                    for (Map.Entry<String,List<OrderDetail>> entry : groupedByMerchant.entrySet()) {
                        OrderPerMerchant orderPerMerchant = new OrderPerMerchant();

                        orderPerMerchant.setMerchantName(entry.getKey());
                        List<OrderDetailWithProduct> orderDetailWithProducts = new ArrayList<>();
                        for (OrderDetail orderDetail : entry.getValue()){
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
