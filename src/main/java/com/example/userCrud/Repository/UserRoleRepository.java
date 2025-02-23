package com.example.userCrud.Repository;

import com.example.userCrud.Entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoles, Integer> {

}
