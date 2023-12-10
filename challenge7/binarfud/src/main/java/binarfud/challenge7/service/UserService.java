package binarfud.challenge7.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import binarfud.challenge7.client.UserClient;
import binarfud.challenge7.dto.user.response.FindUserDto;
import binarfud.challenge7.model.Merchant;
import binarfud.challenge7.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient client;

    public FindUserDto updateMerchant(String token, Merchant merchant) {
        Map<String, UUID> userData = new HashMap<>();
        userData.put("merchantId", merchant.getId());
        
        return client.updateUserMerchant(token, userData);
    }

    public FindUserDto deleteMerchant(String token) {        
        return client.deleteMerchant(token);
    }

    public Optional<User> getUserByToken(String token){
        var user = client.findUser(token);

        if (!user.getStatus().equals("success")) return Optional.of(null);
        if (user.getData() == null) return Optional.of(null);


        return Optional.of(user.getData().getUsers());
    }

}