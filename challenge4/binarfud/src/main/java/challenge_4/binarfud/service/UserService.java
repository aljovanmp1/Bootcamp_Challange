package challenge_4.binarfud.service;

import java.util.List;

import org.springframework.stereotype.Service;

import challenge_4.binarfud.model.User;
import challenge_4.binarfud.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public void setUserEmail(User user, String email) {
        userRepository.setEmailByUserId(user.getId(), email);
    }

    public void setUserPassword(User user, String pwd) {
        userRepository.setPasswordByUserId(user.getId(), pwd);
    }

    public void deleteUser(User user) {
        userRepository.deleteUserById(user.getId());
    }


}