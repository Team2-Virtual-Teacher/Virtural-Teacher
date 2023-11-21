package com.alpha53.virtualteacher.models;

public class Role {
    private int roleId;
    private UserRoles role;

    public Role(int roleId, UserRoles role) {
        this.roleId = roleId;
        this.role = role;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
