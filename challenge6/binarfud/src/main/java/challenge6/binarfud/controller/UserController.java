package challenge6.binarfud.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import challenge6.binarfud.dto.auth.request.AddUserDto;
import challenge6.binarfud.model.User;
import challenge6.binarfud.service.UserService;
import challenge6.binarfud.utlis.DataNotFoundException;
import challenge6.binarfud.utlis.ResourceAlreadyExistException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapperSkipNull;

    // @PostMapping
    // public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody AddUserDto dataDto) {
    //     Map<String, Object> response = new HashMap<>();
    //     Map<String, Object> data = new HashMap<>();
    //     try {
    //         Optional<User> existUser = userService.getUserByUserName(dataDto.getUsername());
    //         if (existUser.isPresent())
    //             throw new ResourceAlreadyExistException("username already taken");

    //         User newUser = new User();
    //         newUser.setUsername(dataDto.getUsername());
    //         newUser.setEmailAddress(dataDto.getEmail());
    //         newUser.setPassword(dataDto.getPwd());

    //         newUser = userService.addOrUpdateUser(newUser);

    //         data.put("merchants", newUser);
    //         response.put("data", data);
    //         response.put("status", "success");

    //         return new ResponseEntity<>(response, HttpStatus.CREATED);

    //     } catch (ResourceAlreadyExistException e) {
    //         response.put("status", "fail");
    //         data.put("user", e.getMessage());
    //         response.put("data", data);
    //         return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    //     }

    // }

    @PutMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> put(@RequestParam UUID id,
            @RequestBody User user) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        user.setId(id);
        data.put("user", userService.addOrUpdateUser(user));
        response.put("data", data);
        response.put("status", "success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Map<String, Object>> patch(@RequestParam UUID id,
            @RequestBody User user) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Optional<User> selectedUser = userService.getUserById(id);
            if (!selectedUser.isPresent())
                throw new DataNotFoundException("User not found");

            User updatedUser = selectedUser.get();
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
