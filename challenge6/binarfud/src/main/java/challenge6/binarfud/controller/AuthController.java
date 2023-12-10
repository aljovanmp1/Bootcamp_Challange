package challenge6.binarfud.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import challenge6.binarfud.dto.ResponseHandler;
import challenge6.binarfud.dto.auth.request.AddUserDto;
import challenge6.binarfud.dto.auth.request.ForgetPassword;
import challenge6.binarfud.dto.auth.request.LoginRequest;
import challenge6.binarfud.dto.auth.request.ResetPassword;
import challenge6.binarfud.dto.auth.response.JwtResponse;
import challenge6.binarfud.model.Role;
import challenge6.binarfud.model.User;
import challenge6.binarfud.security.jwt.JwtUtils;
import challenge6.binarfud.security.jwt.OtpJwtDto;
import challenge6.binarfud.security.service.UserDetailsImpl;
import challenge6.binarfud.service.EmailService;
import challenge6.binarfud.service.OtpService;
import challenge6.binarfud.service.RoleService;
import challenge6.binarfud.service.UserService;
import challenge6.binarfud.utlis.DataNotFoundException;
import challenge6.binarfud.utlis.ResourceAlreadyExistException;
import challenge6.binarfud.utlis.RoleNotExistException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    OtpService otpService;

    @Autowired
    EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), roles);
        return ResponseHandler.generateResponse("success", jwtResponse, null, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody AddUserDto dataDto) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Optional<User> existUser = userService.getUserByUserName(dataDto.getUsername());
            if (existUser.isPresent())
                throw new ResourceAlreadyExistException("username already taken");

            List<Role> availableRole = roleService.getRoles();
            List<String> roleNoId = availableRole.stream()
                    .map(role -> role.getName().toString())
                    .collect(Collectors.toList());

            boolean allRolesMatch = dataDto.getRoles().stream()
                    .allMatch(roleNoId::contains);

            if (!allRolesMatch)
                throw new RoleNotExistException("Role not found, available role -> " + roleNoId.toString());

            Set<Role> roleReq = dataDto.getRoles().stream()
                    .map(role -> {
                        Optional<Role> optRole = roleService.getRole(role);
                        if (optRole.isPresent())
                            return optRole.get();
                        return new Role();
                    })
                    .collect(Collectors.toSet());

            User newUser = new User();
            newUser.setUsername(dataDto.getUsername());
            newUser.setEmailAddress(dataDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(dataDto.getPwd()));
            newUser.setRoles(roleReq);

            newUser = userService.addOrUpdateUser(newUser);

            // data.put("user", newUser);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (ResourceAlreadyExistException | RoleNotExistException e) {
            response.put("status", "fail");
            data.put("user", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/forget-password")
    public ResponseEntity<Map<String, Object>> forgetPwd(@Valid @RequestBody ForgetPassword req) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            Optional<User> existUser = userService.getUserByUserName(req.getUsername());
            if (!existUser.isPresent())
                throw new DataNotFoundException("username not found");
            String email = existUser.get().getEmailAddress();

            String otp = otpService.generateOTP(req.getUsername());
            emailService.sendSimpleMessage(email, "OTP Password Binarfud", "otp: " + otp);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("user", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPwd(@Valid @RequestBody ResetPassword req) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            if (!otpService.validateOtp(req.getOtp()))
                throw new DataNotFoundException("otp is invalid");
            String jwtContent = jwtUtils.getUsername(req.getOtp());

            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, OtpJwtDto>> typeRef = new TypeReference<HashMap<String, OtpJwtDto>>() {};

            HashMap<String, OtpJwtDto> o = mapper.readValue(jwtContent, typeRef);
            OtpJwtDto userData = o.get("data");

            Optional<User> existUser = userService.getUserByUserName(userData.getUsername());
            if (!existUser.isPresent())
                throw new DataNotFoundException("username not found");

            User user = existUser.get();
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            userService.addOrUpdateUser(user);

            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("user", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch(JsonProcessingException e){
            response.put("status", "fail");
            data.put("user", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
