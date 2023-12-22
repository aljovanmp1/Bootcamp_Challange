package binarfud.challenge8.dto.user.response;

import binarfud.challenge8.model.User;
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
