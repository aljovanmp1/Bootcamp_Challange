package challenge_4.binarfud.service;

import java.util.Optional;
import java.util.List;

import challenge_4.binarfud.utlis.Constants;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import challenge_4.binarfud.model.Merchant;
import challenge_4.binarfud.repository.MerchantRepository;
import challenge_4.binarfud.utlis.DataNotFoundException;

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

    public void addMerchant(Merchant merchant){
        merchantRepository.save(merchant);
    }

    public List<Merchant> getAllMerchants(){
        return merchantRepository.findAll();
    }

    public void setMerchantAvailability(Merchant merchant){
        boolean newState = Boolean.TRUE.equals(merchant.getOpen());  
        merchant.setOpen(!newState);
        merchantRepository.save(merchant);
    }

    public Page<Merchant> getAllOpenMerchants(int page){
        return merchantRepository.findByOpenEquals(true, PageRequest.of(page, 5));
    }
}


