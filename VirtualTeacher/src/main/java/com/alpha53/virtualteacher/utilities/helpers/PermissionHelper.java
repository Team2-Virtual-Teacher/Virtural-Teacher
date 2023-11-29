package com.alpha53.virtualteacher.utilities.helpers;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.models.User;

public class PermissionHelper {

    public static void validateIsCreator(User creator, User currentUser, RuntimeException runtimeException){
        if (creator.getUserId() != currentUser.getUserId()){
            throw runtimeException;
        }
    }
    public static void validateIsAdmin(User user, String message){
        if (!user.getRole().getRoleType().equalsIgnoreCase("Admin")){
            throw new AuthorizationException(message);
        }
    }
}
