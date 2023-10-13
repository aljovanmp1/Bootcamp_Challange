package binarfud.challenge3.service;

import java.util.Optional;


import binarfud.challenge3.utlis.Constants;
import org.springframework.stereotype.Service;

import binarfud.challenge3.model.Merchant;
import binarfud.challenge3.repository.MerchantRepository;
import binarfud.challenge3.utlis.DataNotFoundException;

@Service
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant getOneMerchant(Long id) throws DataNotFoundException{
        Optional<Merchant> merchantOptional = merchantRepository.findById(id);
        
        if (merchantOptional.isPresent()) return merchantOptional.get();
        throw new DataNotFoundException(Constants.ERR_DATANOTFOUND);
    }

    public void initData(){
        if (merchantRepository.count() == 0){
            Merchant merchant = new Merchant();
            merchant.setMerchantName("binarfud");
            merchantRepository.save(merchant);
        }
    }
}
