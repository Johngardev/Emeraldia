package com.emeraldia.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {
  @Id
  private String id;

  @Indexed(unique = true) // Asegura que el email sea único en la base de datos
  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;

  @NotBlank(message = "User name cannot be empty")
  @Size(min = 2, max = 50, message = "User name must be between 2 and 50 characters")
  private String username;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  private Boolean isActive;
  private String phoneNumber;
  private String address;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toSet());
  }

  /**
   * Retorna el nombre de usuario (en nuestro caso, el username).
   * @return El username del usuario.
   */
  @Override
  public String getUsername() {
    return this.username;
  }

  /**
   * Indica si la cuenta del usuario ha expirado.
   * Por ahora, lo establecemos en 'true' para que nunca expire.
   * @return Siempre true.
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indica si el usuario está bloqueado o desbloqueado.
   * Por ahora, lo establecemos en 'true' para que nunca esté bloqueado.
   * @return Siempre true.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indica si las credenciales del usuario (contraseña) han expirado.
   * Por ahora, lo establecemos en 'true' para que nunca expiren.
   * @return Siempre true.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indica si el usuario está habilitado o deshabilitado.
   * Usa el campo 'isActive' para controlar esto.
   * @return El valor de isActive.
   */
  @Override
  public boolean isEnabled() {
    return this.isActive != null && this.isActive; // Asegura que no sea nulo y sea true
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public @NotBlank(message = "Email cannot be empty") @Email(message = "Email should be valid") String getEmail() {
    return email;
  }

  public void setEmail(@NotBlank(message = "Email cannot be empty") @Email(message = "Email should be valid") String email) {
    this.email = email;
  }

  public @NotBlank(message = "Password cannot be empty") @Size(min = 8, message = "Password must be at least 8 characters long") String getPassword() {
    return password;
  }

  public void setPassword(@NotBlank(message = "Password cannot be empty") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
    this.password = password;
  }

  public void setUsername(@NotBlank(message = "User name cannot be empty") @Size(min = 2, max = 50, message = "User name must be between 2 and 50 characters") String username) {
    this.username = username;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean active) {
    isActive = active;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
