package com.alpha53.virtualteacher.utilities.helpers;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String INVALID_AUTHENTICATION = "Invalid authentication!";
    public static final String PROFILE_CONFIRMATION_EXCEPTION = "Your profile has not been confirmed. Please follow the link sent to your email.";

    public final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        try {
            if (!headers.containsKey("Authorization")) {
                throw new AuthorizationException(INVALID_AUTHENTICATION);
            }

            String userDetail = headers.getFirst("Authorization");
            if (userDetail == null || userDetail.isBlank()) {
                throw new AuthorizationException(INVALID_AUTHENTICATION);
            }

            String email = getUserEmail(userDetail);
            String userPass = getUserPass(userDetail);

            User user = userService.get(email);

            if (!user.getPassword().equals(userPass)) {
                throw new AuthorizationException(INVALID_AUTHENTICATION);
            }
            if (!user.isVerified()){
                throw new AuthorizationException(PROFILE_CONFIRMATION_EXCEPTION);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION);
        }

    }

    private String getUserEmail(String userDetails) {
        String[] result = userDetails.split(" ");
        if (result.length != 2) {
            throw new AuthorizationException(INVALID_AUTHENTICATION);
        }
        return result[0];
    }

    private String getUserPass(String userDetails) {
        String[] result = userDetails.split(" ");
        if (result.length != 2) {
            throw new AuthorizationException(INVALID_AUTHENTICATION);
        }
        return result[1];

    }

}
