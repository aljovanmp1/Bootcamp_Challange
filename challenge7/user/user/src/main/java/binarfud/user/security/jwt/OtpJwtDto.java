package binarfud.user.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OtpJwtDto {
    String username;
    String otp;
}
