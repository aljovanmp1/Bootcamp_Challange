package challange_5.binarfud.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import challange_5.binarfud.model.User;
import challange_5.binarfud.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addOrUpdateUser(User user) {
        return userRepository.save(user);
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

    public void hardDeleteUser(User user) {
        userRepository.deleteUserById(user.getId());
    }

    public User deleteUser(User user) {
        user.setDeleted(true);
        return userRepository.save(user);
    }

    public Optional<User> getUserByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
    public Optional<User> getUserById(UUID id){
        return userRepository.findByIdAndIsDeleted(id, false);
    }


}