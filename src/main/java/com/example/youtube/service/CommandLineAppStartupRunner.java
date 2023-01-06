package com.example.youtube.service;

import com.example.youtube.repository.ProfileRepository;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final ProfileRepository repository;

    private final DataSource dataSource;

    public CommandLineAppStartupRunner(ProfileRepository repository, DataSource dataSource) {
        this.repository = repository;
        this.dataSource = dataSource;
    }


    @Override
    public void run(String... args) throws Exception {

        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();

//        Optional<ProfileEntity> exists = repository.findByEmail("admin@gmail.com");
//
//        if (exists.isPresent()) {
//            return;
//        }
//        ProfileEntity entity = new ProfileEntity();
//        entity.setName("Admin");
//        entity.setSurname("Adminjon");
//        entity.setEmail("admin@gmail.com");
//        entity.setRole(ProfileRole.ROLE_ADMIN);
//        entity.setPassword(MD5.md5("200622az"));
//        entity.setCreatedDate(LocalDateTime.now());
//        entity.setStatus(ProfileStatus.ACTIVE);
//
//        repository.save(entity);

    }
}
