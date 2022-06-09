package com.arch.ihcd.gateway.repository;

import com.arch.ihcd.gateway.bean.Privilege;
import com.arch.ihcd.gateway.bean.Role;
import com.arch.ihcd.gateway.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Delete all
        /*this.userRepository.deleteAll();

        Role ADMIN = new Role();
        ADMIN.setRole("ADMIN");
        Role MANAGER = new Role();
        MANAGER.setRole("MANAGER");
        Role USER = new Role();
        USER.setRole("USER");

        Privilege ADMIN_ACCESS1 = new Privilege();
        ADMIN_ACCESS1.setPrivilege("ADMIN_ACCESS1");
        Privilege ADMIN_ACCESS2 = new Privilege();
        ADMIN_ACCESS2.setPrivilege("ADMIN_ACCESS2");
        Privilege MANAGER_ACCESS1 = new Privilege();
        MANAGER_ACCESS1.setPrivilege("MANAGER_ACCESS1");
        Privilege MANAGER_ACCESS2 = new Privilege();
        MANAGER_ACCESS2.setPrivilege("MANAGER_ACCESS2");
        Privilege USER_ACCESS1 = new Privilege();
        USER_ACCESS1.setPrivilege("USER_ACCESS1");
        Privilege USER_ACCESS2 = new Privilege();
        USER_ACCESS2.setPrivilege("USER_ACCESS2");

       User edu = new User();
       edu.setUsername("edu");
       edu.setPassword(passwordEncoder.encode("edu123"));
       edu.setEmail("edu@test.com");
       edu.setRoles(new HashSet<>(Arrays.asList(MANAGER, USER)));
       edu.setPrivileges(new HashSet<>(Arrays.asList(MANAGER_ACCESS1, MANAGER_ACCESS2, USER_ACCESS1, USER_ACCESS2)));

       User adminUser = new User();
       adminUser.setUsername("admin");
       adminUser.setPassword(passwordEncoder.encode("admin123"));
       adminUser.setEmail("admin@test.com");
       adminUser.setRoles(new HashSet<>(Arrays.asList(ADMIN)));
       adminUser.setPrivileges(new HashSet<>(Arrays.asList(ADMIN_ACCESS1, ADMIN_ACCESS2)));


        User user = new User();
        user.setUsername("egumma");
        user.setPassword(passwordEncoder.encode("egumma123"));
        user.setEmail("admin@test.com");
        user.setRoles(new HashSet<>(Arrays.asList(USER)));
        user.setPrivileges(new HashSet<>(Arrays.asList(USER_ACCESS1, USER_ACCESS2)));


        List<User> users = Arrays.asList(edu, adminUser, user);

        this.userRepository.saveAll(users);*/
    }
}
