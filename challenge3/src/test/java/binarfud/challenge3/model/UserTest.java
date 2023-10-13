package binarfud.challenge3.model;

import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    
    User user = new User();

    @Test
    @DisplayName("Positive Test - User Getter")
    void testUserGetter(){
        User user = new User();

        user.setId(1L);
        user.setEmailAddress("email@example.com");
        user.setPassword("example");
        user.setUserName("user");

        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("email@example.com", user.getEmailAddress());
        Assertions.assertEquals("example", user.getPassword());
        Assertions.assertEquals("user", user.getUserName());
    }
}
