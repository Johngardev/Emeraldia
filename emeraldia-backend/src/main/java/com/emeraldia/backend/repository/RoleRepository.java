package com.emeraldia.backend.repository;

import com.emeraldia.backend.model.EmployeeRole;
import com.emeraldia.backend.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(EmployeeRole name);

  @Override
  List<Role> findAllById(Iterable<String> strings);
}
