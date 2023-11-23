package challenge6.binarfud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

import challenge6.binarfud.dto.merchant.others.OrderAnalysisDto;
import challenge6.binarfud.dto.merchant.request.AddMerchantDto;
import challenge6.binarfud.dto.merchant.request.GetMerchantsDto;
import challenge6.binarfud.dto.merchant.request.GetOrderAnalysisDto;
import challenge6.binarfud.model.Merchant;
import challenge6.binarfud.model.User;
import challenge6.binarfud.service.MerchantService;
import challenge6.binarfud.service.OrderAnalysisService;
import challenge6.binarfud.service.UserService;
import challenge6.binarfud.utlis.DataNotFoundException;
import challenge6.binarfud.utlis.ResourceAlreadyExistException;
import challenge6.binarfud.utlis.Utils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/merchants")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ModelMapper modelMapperSkipNull;

    @Autowired
    OrderAnalysisService orderAnalysisService;

    @GetMapping()
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> getMerchant(@Valid GetMerchantsDto params,
            Authentication authentication) {
        Optional<Boolean> isOpen = Optional.ofNullable(params.getOpen());
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Integer selectedPage = params.getSelectedPage();
        Integer dataPerPage = params.getDataPerPage();

        if (isOpen.isPresent()) {
            Page<Merchant> merchantData = merchantService.getMerchantFilterByOpen(
                    isOpen.get(),
                    selectedPage - 1,
                    dataPerPage);
            int totalPage = merchantData.getTotalPages();
            if (totalPage < selectedPage && totalPage != 0) {
                selectedPage = totalPage;
                merchantData = merchantService.getMerchantFilterByOpen(
                        isOpen.get(),
                        selectedPage - 1,
                        dataPerPage);
            }

            data.put("merchant", merchantData.getContent());
            Utils.addPageResponse(merchantData, response);
            response.put("data", data);
            response.put("status", "success");
        } else {
            Page<Merchant> merchantData = merchantService.getAllMerchants(
                    selectedPage - 1,
                    dataPerPage);
            int totalPage = merchantData.getTotalPages();
            if (totalPage < selectedPage && totalPage != 0) {
                selectedPage = totalPage;
                merchantData = merchantService.getAllMerchants(
                        selectedPage - 1,
                        dataPerPage);
            }

            data.put("merchants", merchantData.getContent());
            Utils.addPageResponse(merchantData, response);
            response.put("data", data);
            response.put("status", "success");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> addMerchant(@Valid @RequestBody AddMerchantDto dataDto,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (merchantExistingOpt.isPresent())
                throw new ResourceAlreadyExistException(
                        "User has already have a merchant: " + merchantExistingOpt.get().getMerchantName());

            Merchant newMerchant = new Merchant();
            newMerchant.setMerchantName(dataDto.getMerchantName());
            newMerchant.setMerchantLocation(dataDto.getMerchantLocation());
            newMerchant.setOpen(true);

            newMerchant = merchantService.addOrUpdateMerchant(newMerchant);

            user.setMerchant(newMerchant);
            userService.addOrUpdateUser(user);

            data.put("merchants", newMerchant);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResourceAlreadyExistException e) {
            response.put("status", "fail");
            data.put("merchant", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> putMerchant(@RequestBody Merchant merchant,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        String username = authentication.getName();
        User user = userService.getUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
        if (merchantExistingOpt.isPresent())
            merchant.setId(merchantExistingOpt.get().getId());

        data.put("merchants", merchantService.addOrUpdateMerchant(merchant));
        response.put("data", data);
        response.put("status", "success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PatchMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> patchMerchant(@RequestBody Merchant merchant,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            Merchant updatedMerchant = merchantExistingOpt.get();
            modelMapperSkipNull.map(merchant, updatedMerchant);

            data.put("merchants", merchantService.addOrUpdateMerchant(updatedMerchant));
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("merchant", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> putMerchant(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            data.put("merchants", merchantService.deleteMerchant(merchantExistingOpt.get()));
            user.setMerchant(null);
            userService.addOrUpdateUser(user);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("merchant", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @GetMapping("order-analysis")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> getAnalysis(@Valid GetOrderAnalysisDto params, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            OrderAnalysisDto orderDto = orderAnalysisService.retrieveAnalysisData(params.getPeriod(),
                    merchantExistingOpt.get());

            ByteArrayOutputStream target = new ByteArrayOutputStream();

            HtmlConverter.convertToPdf(orderAnalysisService.getHtmlStr(orderDto), target);
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
