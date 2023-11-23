package challenge6.binarfud.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.action.internal.OrphanRemovalAction;
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

import challenge6.binarfud.dto.ResponseHandler;
import challenge6.binarfud.dto.auth.request.AddUserDto;
import challenge6.binarfud.dto.auth.request.LoginRequest;
import challenge6.binarfud.dto.auth.response.JwtResponse;
import challenge6.binarfud.model.Role;
import challenge6.binarfud.model.User;
import challenge6.binarfud.security.jwt.JwtUtils;
import challenge6.binarfud.security.service.UserDetailsImpl;
import challenge6.binarfud.service.RoleService;
import challenge6.binarfud.service.UserService;
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        System.out.println("username: " + loginRequest.getUsername());
        System.out.println("password: " + loginRequest.getPassword());
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

            data.put("merchants", newUser);
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
}
