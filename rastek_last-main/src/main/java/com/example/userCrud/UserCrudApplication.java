package com.example.userCrud;

import com.example.userCrud.Dto.CreateUserRequest;
import com.example.userCrud.Entity.User;
import com.example.userCrud.Entity.UserProfile;
import com.example.userCrud.Entity.Roles;
import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class UserCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCrudApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;


	@Bean
	public CommandLineRunner initDatabase() {
		return args -> {
			CreateUserRequest adminRequest = CreateUserRequest.builder()
					.username("Admin")
					.password("Lontongsayur@39")
					.email("tetew39@gmail.com")
					.idRoles(null)
					.build();

			// Cek apakah user dengan username "Admin" sudah ada
			if (userRepository.findByUsername(adminRequest.getUsername()).isEmpty()) {
				// Enkripsi password dari DTO
				String salt = BCrypt.gensalt();
				String encodedPassword = BCrypt.hashpw(adminRequest.getPassword(), salt);

				// Buat user baru dari data DTO
				User adminUser = new User();
				adminUser.setUsername(adminRequest.getUsername());
				adminUser.setPassword(encodedPassword);
				adminUser.setEmail(adminRequest.getEmail());

				// Simpan user
				userRepository.save(adminUser);
				System.out.println("Admin user created with encrypted password and default role");
			} else {
				System.out.println("Admin user already exists, no new user added");
			}
		};
	}
}
