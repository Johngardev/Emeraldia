package com.emeraldia.backend.service;

import com.emeraldia.backend.exception.ResourceNotFoundException;
import com.emeraldia.backend.model.User;
import com.emeraldia.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Registra un nuevo usuario en la base de datos.
   * La contraseña se hashea antes de guardarla.
   * @param user El objeto User a registrar.
   * @return El usuario registrado con la contraseña hasheada.
   * @throws IllegalArgumentException Si el email ya está registrado.
   */
  public User registerUser(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("Email already registered: " + user.getEmail());
    }
    // Hashear la contraseña antes de guardarla
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // Asignar un rol por defecto si no se especifican (ej. ROLE_USER)
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
      user.setRoles(Set.of());
    }
    // Habilitar la cuenta por defecto
    if (user.getIsActive() == null) {
      user.setIsActive(true);
    }
    return userRepository.save(user);
  }

  /**
   * Obtiene todos los usuarios.
   * @return Una lista de todos los usuarios.
   */
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Obtiene un usuario por su ID.
   * @param id El ID del usuario.
   * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
   */
  public Optional<User> getUserById(String id) {
    return userRepository.findById(id);
  }

  /**
   * Obtiene un usuario por su email.
   * @param email El email del usuario.
   * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
   */
  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Actualiza la información de un usuario existente.
   * @param id El ID del usuario a actualizar.
   * @param userDetails Los nuevos detalles del usuario.
   * @return El usuario actualizado.
   * @throws ResourceNotFoundException Si el usuario no se encuentra.
   */
  public User updateUser(String id, User userDetails) {
    User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    // Actualizar campos permitidos
    existingUser.setFirstName(userDetails.getFirstName());
    existingUser.setLastName(userDetails.getLastName());
    existingUser.setPhoneNumber(userDetails.getPhoneNumber());
    existingUser.setAddress(userDetails.getAddress());
    existingUser.setIsActive(userDetails.getIsActive());
    // Solo actualizar roles si se proporcionan nuevos roles, o si es un admin.
    // La lógica de actualización de roles podría ser más compleja y necesitar validación de quién puede cambiar qué rol.
    if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
      existingUser.setRoles(userDetails.getRoles());
    }

    // Si la contraseña se proporciona y no está vacía, hashearla y actualizarla
    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
    }
    // No permitir cambiar el email desde aquí para evitar conflictos de unicidad.
    // Si el email necesita ser cambiado, sería un endpoint/método separado con verificación.

    return userRepository.save(existingUser);
  }

  /**
   * Elimina un usuario por su ID.
   * @param id El ID del usuario a eliminar.
   * @throws ResourceNotFoundException Si el usuario no se encuentra.
   */
  public void deleteUser(String id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }
}
