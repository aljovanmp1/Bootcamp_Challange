package binarfud.challenge7.dto.auth.request;

import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AddUserDto {
    @NotNull
    private String email;
    @NotNull
    private String pwd;
    @NotNull
    private String username;
    @NotNull
    private Set<String> roles;
}
