package binarfud.challenge8.dto.auth.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ForgetPassword {
    @NotNull
    private String username;
}
