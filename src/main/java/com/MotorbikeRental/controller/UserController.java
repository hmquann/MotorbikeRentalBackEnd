package com.MotorbikeRental.controller;

import com.MotorbikeRental.dto.UserDto;
import com.MotorbikeRental.entity.User;
import com.MotorbikeRental.exception.UserNotFoundException;
import com.MotorbikeRental.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi user");
    }

    @GetMapping("/allUser/{page}/{pageSize}")
    public Page<UserDto> getAllUser(@PathVariable int page, @PathVariable int pageSize){
        return userService.getAllUser(page,pageSize);
    }

//    @GetMapping("/allUser/{page}/{pageSize}")
//    public ResponseEntity<Page<User>> listBrandWithPagination(@PathVariable int page, @PathVariable int pageSize) {
//        Page<User> userPage = userService.getUserByPagination(page,pageSize);
//        return ResponseEntity.ok(userPage);
//    }

    @GetMapping("/search")
    public Page<UserDto> searchUser(
            @RequestParam String searchTerm,
            @RequestParam int page,
            @RequestParam int size) {
        return userService.searchUserByEmailOrPhone(searchTerm, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        UserDto userDto = userService.getUserDtoById(id);
        if(userDto == null) throw new UserNotFoundException("User not found");
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody User user){
        User updatedUser = userService.updateUser(id,user);

        if(updatedUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
            return new ResponseEntity<>("User with ID " + id + " is deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleUser(@PathVariable Long id){
        try {
            userService.toggleUserActiveStatus(id);
            // Return success message based on user's new status
            String message = userService.getUserDtoById(id).isActive() ? "User activated successfully" : "User deactivated successfully";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
