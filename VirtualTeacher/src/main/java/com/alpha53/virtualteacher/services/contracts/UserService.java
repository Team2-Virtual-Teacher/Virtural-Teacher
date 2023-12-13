package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.FilterOptionsUsers;
import com.alpha53.virtualteacher.models.Role;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User get(int id);

    User get(String email);

    List<User> getAll(FilterOptionsUsers filterOptionsUsers);

    void create(User user, String userRole);

    void update(UserDto userDto, User user, int id);

    void delete(int id, User user);

    void setUserRole(User loggedInUser, User userToGetRole, String roleName);

    void uploadProfilePicture(MultipartFile file, User user, int id);

    void confirmRegistration(String token);

    void referFriend(User loggedInUser, String email);

    List<Role> getRoles();
}
