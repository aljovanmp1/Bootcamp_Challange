package challenge_4.binarfud.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import challenge_4.binarfud.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Procedure("set_pwd_by_user_id")
    void setPasswordByUserId(UUID id, String password);

    @Procedure("set_email_by_user_id")
    void setEmailByUserId(UUID id, String email);

    @Procedure("delete_user_by_id")
    void deleteUserById(UUID id);

} 