package binarfud.challenge8.client;

import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import binarfud.challenge8.dto.user.response.FindUserDto;
import feign.Headers;

@FeignClient(name = "userClient", url = "${spring.cloud.openfeign.client.config.userClient.url}")
public interface UserClient {

    @GetMapping("/user/private")
    @Headers("Authorization: {token}")
    FindUserDto findUser(@RequestHeader("Authorization") String token);

    @PostMapping("/user/private/merchant")
    @Headers("Authorization: {token}")
    FindUserDto updateUserMerchant(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, UUID> requestBody);
    
    @DeleteMapping("/user/private/merchant")
    @Headers("Authorization: {token}")
    FindUserDto deleteMerchant(@RequestHeader("Authorization") String token);
}
