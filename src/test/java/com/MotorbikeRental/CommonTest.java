//package com.MotorbikeRental;
//
//import com.MotorbikeRental.dto.JwtAuthenticationResponse;
//import com.MotorbikeRental.dto.SigninRequest;
//import com.MotorbikeRental.dto.SignupRequest;
//import com.MotorbikeRental.entity.Role;
//import com.MotorbikeRental.entity.User;
//import com.MotorbikeRental.exception.DuplicateUserException;
//import com.MotorbikeRental.exception.InactiveUserException;
//import com.MotorbikeRental.exception.InvalidCredentialsException;
//import com.MotorbikeRental.repository.RoleRepository;
//import com.MotorbikeRental.repository.UserRepository;
//import com.MotorbikeRental.service.JWTService;
//import com.MotorbikeRental.service.impl.AuthenticationServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.util.AssertionErrors;
//import java.util.HashMap;
//import java.util.Optional;
//import java.util.Set;
//
//@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringReactApplication.class)
//class CommonTest {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    void contextLoads() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testSignUp_UserAlreadyExistsByEmail() {
//        SignupRequest signupRequest = new SignupRequest();
//        String email = "abc@example.com";
//        signupRequest.setEmail(email);
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, null, null, null, null);
//        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
//        Assertions.assertThrows(DuplicateUserException.class, () -> authenticationService.signUp(signupRequest));
//        Mockito.verify(userRepository).existsByEmail(email);
//    }
//
//    @Test
//    public void testSignUp_UserAlreadyExistsByPhone() {
//        SignupRequest signupRequest = new SignupRequest();
//        signupRequest.setEmail("test@example.com");
//        signupRequest.setPhone("1234567890");
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
////        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, null, null, roleRepository);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, null, null, null, null, null);
//        Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
//        Mockito.when(userRepository.existsByPhone(signupRequest.getPhone())).thenReturn(true);
//
//        Assertions.assertThrows(DuplicateUserException.class, () -> authenticationService.signUp(signupRequest));
//    }
//
//    @Test
//    public void testSignUp_Success() {
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, null, null, null, null);
//        SignupRequest signupRequest = new SignupRequest();
//        signupRequest.setEmail("test@example.com");
//        signupRequest.setPhone("1234567890");
//        signupRequest.setFirstname("John");
//        signupRequest.setLastname("Doe");
//        signupRequest.setPassword("password");
//        signupRequest.setGender(true);
//
//        Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
//        Mockito.when(userRepository.existsByPhone(signupRequest.getPhone())).thenReturn(false);
//        Mockito.when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
//        Mockito.when(roleRepository.findByName("USER")).thenReturn(new Role("USER"));
//        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(new User());
//
//        User result = authenticationService.signUp(signupRequest);
//
//        AssertionErrors.assertNotNull("", result);
//    }
//
//    @Test
//    public void testSignin_InvalidEmailOrPhone() {
//        SigninRequest signinRequest = new SigninRequest();
//        signinRequest.setEmailOrPhone("test@example.com");
//        signinRequest.setPassword("password");
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, null, null, null, null);
//
//        Mockito.when(userRepository.findByEmailOrPhone(signinRequest.getEmailOrPhone())).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(InvalidCredentialsException.class, () -> authenticationService.signin(signinRequest));
//    }
//
//    @Test
//    public void testSignin_UserInactive() {
//        SigninRequest signinRequest = new SigninRequest();
//        signinRequest.setEmailOrPhone("test@example.com");
//        signinRequest.setPassword("password");
//
//        User user = new User();
//        user.setActive(false);
//
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, null, null, null, null);
//
//        Mockito.when(userRepository.findByEmailOrPhone(signinRequest.getEmailOrPhone())).thenReturn(Optional.of(user));
//        Assertions.assertThrows(InactiveUserException.class, () -> authenticationService.signin(signinRequest));
//    }
//
//    @Test
//    public void testSignin_InvalidPassword() {
//        SigninRequest signinRequest = new SigninRequest();
//        signinRequest.setEmailOrPhone("test@example.com");
//        signinRequest.setPassword("password");
//
//        User user = new User();
//        user.setActive(true);
//        user.setPassword("encodedPassword");
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, null, null, null,null);
//
//        Mockito.when(userRepository.findByEmailOrPhone(signinRequest.getEmailOrPhone())).thenReturn(Optional.of(user));
//        Mockito.when(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())).thenReturn(false);
//
//        Assertions.assertThrows(InvalidCredentialsException.class, () -> authenticationService.signin(signinRequest));
//    }
//
//    @Test
//    public void testSignin_Success() {
//        SigninRequest signinRequest = new SigninRequest();
//        signinRequest.setEmailOrPhone("test@example.com");
//        signinRequest.setPassword("password");
//
//        User user = new User();
//        user.setActive(true);
//        user.setPassword("encodedPassword");
//        user.setRoles(Set.of(new Role("USER")));
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
//        JWTService jwtService = Mockito.mock(JWTService.class);
//        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
//        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, authenticationManager, jwtService, roleRepository);
//
//        Mockito.when(userRepository.findByEmailOrPhone(signinRequest.getEmailOrPhone())).thenReturn(Optional.of(user));
//        Mockito.when(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())).thenReturn(true);
//        Mockito.when(jwtService.generateToken(user)).thenReturn("jwtToken");
//        Mockito.when(jwtService.generateRefreshToken(ArgumentMatchers.any(HashMap.class), ArgumentMatchers.any(User.class))).thenReturn("refreshToken");
//
//        JwtAuthenticationResponse response = authenticationService.signin(signinRequest);
//
//        AssertionErrors.assertNotNull("", response);
//    }
//
//}