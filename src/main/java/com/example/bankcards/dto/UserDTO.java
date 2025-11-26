package com.example.bankcards.dto;

import java.util.List;
import java.util.Objects;

public class UserDTO {

    private Integer id;

    private String email;

    private Boolean blocked;

    private List<RoleDTO> roles;

    public UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", blocked=" + blocked +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(email, userDTO.email) && Objects.equals(blocked, userDTO.blocked) && Objects.equals(roles, userDTO.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, blocked, roles);
    }
}
