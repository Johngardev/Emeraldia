package com.emeraldia.backend.controller;

import com.emeraldia.backend.exception.ResourceNotFoundException;
import com.emeraldia.backend.model.User;
import com.emeraldia.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Endpoint para el registro de nuevos usuarios.
   * No requiere autenticación.
   * @param user El objeto User con los detalles de registro.
   * @return ResponseEntity con el usuario creado o un error.
   */
  @PostMapping("/register") // Este endpoint no estará protegido inicialmente
  public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
    User registeredUser = userService.registerUser(user);
    return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
  }

  /**
   * Obtiene todos los usuarios.
   * Requiere autenticación y el rol de ADMIN.
   * @return Una lista de usuarios.
   */
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')") // Protegeremos este endpoint después de configurar JWT
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    if (users.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   * Obtiene un usuario por su ID.
   * Requiere autenticación y el rol de ADMIN, o ser el propio usuario.
   * @param id El ID del usuario.
   * @return El usuario encontrado.
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") // Protegeremos este endpoint después
  public ResponseEntity<User> getUserById(@PathVariable String id) {
    User user = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Actualiza un usuario existente.
   * Requiere autenticación y el rol de ADMIN, o ser el propio usuario.
   * @param id El ID del usuario a actualizar.
   * @param userDetails Los nuevos detalles del usuario.
   * @return El usuario actualizado.
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") // Protegeremos este endpoint después
  public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody User userDetails) {
    User updatedUser = userService.updateUser(id, userDetails);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  /**
   * Elimina un usuario.
   * Requiere autenticación y el rol de ADMIN.
   * @param id El ID del usuario a eliminar.
   * @return Respuesta vacía.
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')") // Protegeremos este endpoint después
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}