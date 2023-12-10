package binarfud.challenge7.dto.user.response;

import binarfud.challenge7.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserDto {
    UserData data;
    String status;

    @Getter
    @Setter
    public static class UserData{
        User users;
    }
}
