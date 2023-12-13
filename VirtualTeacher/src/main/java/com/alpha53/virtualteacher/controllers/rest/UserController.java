package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.*;
import com.alpha53.virtualteacher.models.ConfirmationToken;
import com.alpha53.virtualteacher.models.FilterOptionsUsers;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.dtos.UserDto;
import com.alpha53.virtualteacher.models.dtos.UserDtoOut;
import com.alpha53.virtualteacher.services.contracts.StorageService;
import com.alpha53.virtualteacher.services.contracts.UserService;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import com.alpha53.virtualteacher.utilities.mappers.dtoMappers.UserMapperHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapperHelper userMapperHelper;

    @Autowired
    public UserController(UserService userService, AuthenticationHelper authenticationHelper, UserMapperHelper userMapperHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapperHelper = userMapperHelper;
    }

    @GetMapping
    public List<UserDtoOut> getAll(@RequestHeader HttpHeaders headers,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String firstName,
                                   @RequestParam(required = false) String lastName,
                                   @RequestParam(required = false) String roleType,
                                   @RequestParam(required = false) String sortBy,
                                   @RequestParam(required = false) String sortOrder) {
        FilterOptionsUsers filterOptionsUsers = new FilterOptionsUsers(email, firstName, lastName, roleType, sortBy, sortOrder);
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.getAll(filterOptionsUsers)
                    .stream()
                    .map(userMapperHelper::userToUserDtoOut)
                    .collect(Collectors.toList());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public UserDtoOut get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            User userToReturn = userService.get(id);
            return userMapperHelper.userToUserDtoOut(userToReturn);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping()
    public void create(@RequestBody @Valid UserDto userDto) {
        User user = userMapperHelper.userDtoToUser(userDto);
        try {
            String userRole = userDto.getRole();
            userService.create(user, userRole);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/confirm")
    public void confirmRegistration(@RequestParam String token) {
        try {
            userService.confirmRegistration(token);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (RegistrationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserDto userDto, @PathVariable int id) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            userService.update(userDto, loggedInUser, id);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            userService.delete(id, loggedInUser);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (StorageException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("{id}/role/{newRole}")
    public void setUserRole(@RequestHeader HttpHeaders headers, @PathVariable int id, @PathVariable String newRole) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            User userToGetRole = userService.get(id);
            userService.setUserRole(loggedInUser, userToGetRole, newRole);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("{id}/profilePicture")
    public void uploadProfilePicture(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            userService.uploadProfilePicture(file, loggedInUser, id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (StorageException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

@PostMapping("/refer/{email}")
    public void referFriend(@RequestHeader HttpHeaders headers, @PathVariable String email){
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            userService.referFriend(loggedInUser ,email);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
}

}
