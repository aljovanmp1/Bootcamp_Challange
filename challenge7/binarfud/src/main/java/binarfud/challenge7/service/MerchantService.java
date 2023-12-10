package binarfud.challenge7.service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import binarfud.challenge7.dto.merchant.others.OrderAnalysisMonthly;
import binarfud.challenge7.dto.merchant.others.OrderAnalysisWeekly;
import binarfud.challenge7.model.Merchant;
import binarfud.challenge7.repository.MerchantRepository;

@Service
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Optional<Merchant> getOneMerchant(UUID id){
        return merchantRepository.findByIdAndIsDeleted(id, false);
    }

    public void initData(){
        if (merchantRepository.count() == 0){
            Merchant merchant = new Merchant();
            merchant.setMerchantName("binarfud");
            merchantRepository.save(merchant);
        }
    }

    public Merchant addOrUpdateMerchant(Merchant merchant){
        return merchantRepository.save(merchant);
    }

    public Page<Merchant> getAllMerchants(int page, int pageSize){
        return merchantRepository.findByIsDeleted(false, PageRequest.of(page,pageSize));
    }

    public void setMerchantAvailability(Merchant merchant){
        boolean newState = Boolean.TRUE.equals(merchant.getOpen());  
        merchant.setOpen(!newState);
        merchantRepository.save(merchant);
    }

    public Page<Merchant> getMerchantFilterByOpen(Boolean open, int page, int pageSize){
        return merchantRepository.findByOpenAndIsDeleted(open, false, PageRequest.of(page, pageSize));
    }

    public Merchant deleteMerchant(Merchant merchant){
        merchant.setDeleted(true);
        return merchantRepository.save(merchant);
    }

    public List<OrderAnalysisMonthly> getOrderAnalysisMonthly(UUID merchantId){
        return merchantRepository.getOrderAnalysisMonthlyByMerchantId(merchantId);
    }
    public List<OrderAnalysisWeekly> getOrderAnalysisWeekly(UUID merchantId){
        return merchantRepository.getOrderAnalysisWeeklyByMerchantId(merchantId);
    }
}


