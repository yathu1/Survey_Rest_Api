package com.yathu.springboot.restapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {
  private Logger logger =  LoggerFactory.getLogger(getClass());
  private UserDetailsRepository userDetailsRepository;

    public UserDetailsCommandLineRunner(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userDetailsRepository.save(new UserDetails("Yathu", "Admin"));
        userDetailsRepository.save(new UserDetails("Raj", "User"));
        userDetailsRepository.save(new UserDetails("Mark", "Admin"));

      //  List<UserDetails> users = userDetailsRepository.findAll();
        List<UserDetails> users = userDetailsRepository.findByRole("Admin");
        users.forEach(user -> logger.info(user.toString()));

    }
}
