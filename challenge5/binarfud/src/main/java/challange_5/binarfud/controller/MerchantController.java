package challange_5.binarfud.controller;

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

import challange_5.binarfud.model.Merchant;
import challange_5.binarfud.model.dto.AddMerchantDto;
import challange_5.binarfud.model.dto.GetMerchantsDto;
import challange_5.binarfud.model.dto.GetOrderAnalysisDto;
import challange_5.binarfud.model.dto.OrderAnalysisDto;
import challange_5.binarfud.model.dto.OrderAnalysisMonthly;
import challange_5.binarfud.model.dto.OrderAnalysisWeekly;
import challange_5.binarfud.service.MerchantService;
import challange_5.binarfud.service.OrderAnalysisService;
import challange_5.binarfud.utlis.DataNotFoundException;
import challange_5.binarfud.utlis.Utils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/merchants")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ModelMapper modelMapperSkipNull;

    @Autowired
    OrderAnalysisService orderAnalysisService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getMerchant(@Valid GetMerchantsDto params) {
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
    public ResponseEntity<Map<String, Object>> addMerchant(@Valid @RequestBody AddMerchantDto dataDto) {
        Merchant newMerchant = new Merchant();
        newMerchant.setMerchantName(dataDto.getMerchantName());
        newMerchant.setMerchantLocation(dataDto.getMerchantLocation());
        newMerchant.setOpen(false);

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        newMerchant = merchantService.addOrUpdateMerchant(newMerchant);

        data.put("merchants", newMerchant);
        response.put("data", data);
        response.put("status", "success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> putMerchant(@RequestParam UUID id,
            @RequestBody Merchant merchant) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        merchant.setId(id);
        data.put("merchants", merchantService.addOrUpdateMerchant(merchant));
        response.put("data", data);
        response.put("status", "success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Map<String, Object>> patchMerchant(@RequestParam UUID id,
            @RequestBody Merchant merchant) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Optional<Merchant> selectedMerchant = merchantService.getOneMerchant(id);
            if (!selectedMerchant.isPresent())
                throw new DataNotFoundException("Merchant not found");

            Merchant updatedMerchant = selectedMerchant.get();
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
    public ResponseEntity<Map<String, Object>> putMerchant(@RequestParam UUID id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Optional<Merchant> selectedMerchant = merchantService.getOneMerchant(id);
            if (!selectedMerchant.isPresent())
                throw new DataNotFoundException("Merchant not found");

            data.put("merchants", merchantService.deleteMerchant(selectedMerchant.get()));
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
    public ResponseEntity<?> getAnalysis(@Valid GetOrderAnalysisDto params) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        try {
            Optional<Merchant> selectedMerchant = merchantService.getOneMerchant(params.getId());
            if (!selectedMerchant.isPresent())
                throw new DataNotFoundException("Merchant not found");

            OrderAnalysisDto orderDto = orderAnalysisService.retrieveAnalysisData(params.getPeriod(), selectedMerchant.get());
            
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
