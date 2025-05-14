package com.example.userCrud;

import com.example.userCrud.Dto.CreateUserRequest;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.PermissionsRepository;
import com.example.userCrud.Repository.RolesRepository;
import com.example.userCrud.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class UserCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCrudApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private PermissionsRepository permissionsRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public CommandLineRunner initDatabase() {
		return args -> {
			CreateUserRequest adminRequest = CreateUserRequest.builder()
					.username("Admin")
					.password("Lontongsayur@39")
					.email("tetew39@gmail.com")
					.idRoles(null)
					.build();

			// Get or Create the Admin user
			User adminUser = userRepository.findByUsername(adminRequest.getUsername())
					.orElseGet(() -> {
						// Encrypt password from DTO
						String salt = BCrypt.gensalt();
						String encodedPassword = BCrypt.hashpw(adminRequest.getPassword(), salt);

						// Buat user baru dari data DTO
						User newUser = new User();
						newUser.setUsername(adminRequest.getUsername());
						newUser.setPassword(encodedPassword);
						newUser.setEmail(adminRequest.getEmail());
						System.out.println("Admin user added");
						return userRepository.save(newUser);
					});

			if (userRepository.findByUsername(adminRequest.getUsername()).isPresent()) {
				System.out.println("Admin user already exist");
			}

			// Eagerly load the roles
			adminUser = entityManager.find(User.class, adminUser.getId());

			// Create or get the Admin role
			Roles adminRole = rolesRepository.findByName("Admin")
					.orElseGet(() -> {
						Roles newAdminRole = new Roles();
						newAdminRole.setName("Admin");
						newAdminRole.setStatus("Active");
						newAdminRole.setCreated_by("System");
						newAdminRole.setCreatedAt(LocalDateTime.now());
						return rolesRepository.save(newAdminRole);
					});

			List<PermissionsEntity> allPermissions = permissionsRepository.findAll();

			// Create RolePermissionEntity objects and associate them with the role
			Set<RolePermissionEntity> rolePermissionSet = new HashSet<>();
			for (PermissionsEntity permission : allPermissions) {
				RolePermissionEntity rolePermission = new RolePermissionEntity();
				rolePermission.setRole(adminRole);
				rolePermission.setPermission(permission);
				rolePermissionSet.add(rolePermission);
			}

			// Assign all permissions to the Admin role
			adminRole.setPermissions(rolePermissionSet);
			rolesRepository.save(adminRole);

			// Assign the role to the user (if not already assigned)
			boolean alreadyAssigned = adminUser.getRoles() != null && adminUser.getRoles().stream()
					.anyMatch(role -> role.getName().equals("Admin"));

			if (!alreadyAssigned) {
				adminUser.setRoles(Collections.singletonList(adminRole));
				userRepository.save(adminUser);
				System.out.println("Admin role assigned to Admin user");
			} else {
				System.out.println("Admin role already assigned to Admin user");
			}
		};
	}
}