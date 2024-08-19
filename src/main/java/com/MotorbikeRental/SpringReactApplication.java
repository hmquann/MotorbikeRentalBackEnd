package com.MotorbikeRental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SpringReactApplication {

//    @Autowired
//    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringReactApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        User adminAccount = userRepository.findByRole(Role.ADMIN);
//
//        if(null == adminAccount){
//            User user = new User();
//
//            user.setEmail("admin@gmail.com");
//            user.setFirstName("admin");
//            user.setLastName("test");
//            user.setRole(Role.ADMIN);
//            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
//            userRepository.save(user);
//        }
//
//    }
}
