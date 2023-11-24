package challenge6.binarfud.dto.auth.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResetPassword {
    @NotNull
    private String otp;
    @NotNull
    private String password;
}
