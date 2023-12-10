package binarfud.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import binarfud.user.dto.auth.request.AddUserDto;
import binarfud.user.dto.user.response.GetUserDto;
import binarfud.user.model.User;
import binarfud.user.service.UserService;
import binarfud.user.utlis.DataNotFoundException;
import binarfud.user.utlis.ResourceAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("user/private")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapperSkipNull;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> get(Authentication authentication, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            GetUserDto userResp = modelMapper.map(user, GetUserDto.class);
            data.put("users", userResp);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UsernameNotFoundException e) {
            response.put("status", "fail");
            data.put("users", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/merchant")
    public ResponseEntity<Map<String, Object>> deleteMerchant(Authentication authentication){
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try{
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new DataNotFoundException("Username not found: " + username));
            
            if (user.getMerchantId() == null) throw new DataNotFoundException("User dont have merchant");
    
            user.setMerchantId(null);
            User userResp = userService.addOrUpdateUser(user);
            response.put("data", data);

            if (userResp.getMerchantId() == null)
                response.put("status", "success");
            else {
                response.put("status", "fail");
                data.put("users", "failed to delete user merchant");
                response.put("data", data);
            }

    
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("users", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    } 

    @PutMapping
    public ResponseEntity<Map<String, Object>> put(@RequestBody User user, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try{
            String username = authentication.getName();
            User updatedUser = userService.getUserByUserName(username)
                    .orElseThrow(() -> new DataNotFoundException("Username not found: " + username));
    
            user.setId(updatedUser.getId());
            data.put("user", userService.addOrUpdateUser(user));
            response.put("data", data);
            response.put("status", "success");
    
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("users", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PostMapping("/merchant")
    public ResponseEntity<Map<String, Object>> editMerchant(@RequestBody User user, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User updatedUser = userService.getUserByUserName(username)
                    .orElseThrow(() -> new DataNotFoundException("Username not found: " + username));

            modelMapperSkipNull.map(user, updatedUser);

            data.put("users", userService.addOrUpdateUser(updatedUser));
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("users", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PatchMapping
    public ResponseEntity<Map<String, Object>> patch(@RequestBody User user, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User updatedUser = userService.getUserByUserName(username)
                    .orElseThrow(() -> new DataNotFoundException("Username not found: " + username));

            modelMapperSkipNull.map(user, updatedUser);

            data.put("users", userService.addOrUpdateUser(updatedUser));
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("users", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam UUID id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Optional<User> selectedUser = userService.getUserById(id);
            if (!selectedUser.isPresent())
                throw new DataNotFoundException("User not found");

            data.put("users", userService.deleteUser(selectedUser.get()));
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("user", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
