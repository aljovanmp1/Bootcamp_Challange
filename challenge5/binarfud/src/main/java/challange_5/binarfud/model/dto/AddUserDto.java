package challange_5.binarfud.model.dto;

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
}
