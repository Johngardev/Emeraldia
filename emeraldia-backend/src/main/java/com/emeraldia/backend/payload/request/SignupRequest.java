package com.emeraldia.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class SignupRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  private String email;

  private Set<String> roles;

  @NotBlank
  @Size(min = 8, max = 50)
  private String password;

  public @NotBlank @Size(min = 3, max = 20) String getUsername() {
    return username;
  }

  public void setUsername(@NotBlank @Size(min = 3, max = 20) String username) {
    this.username = username;
  }

  public @NotBlank @Size(max = 50) String getEmail() {
    return email;
  }

  public void setEmail(@NotBlank @Size(max = 50) String email) {
    this.email = email;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public @NotBlank @Size(min = 8, max = 50) String getPassword() {
    return password;
  }

  public void setPassword(@NotBlank @Size(min = 8, max = 50) String password) {
    this.password = password;
  }
}
