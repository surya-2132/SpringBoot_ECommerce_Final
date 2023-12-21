package com.fullstackproject.ecommerce.Controller;

import com.fullstackproject.ecommerce.Model.User;
import com.fullstackproject.ecommerce.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getuser/{name}")
    public String getUserByName (@PathVariable("name") String user){
        return userService.getUserByName(user);
    }

    @GetMapping("/getallusers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(value = "/registration")
    public String getSignUp(String userName, String email, String password1, String password2 ){
        return userService.signUp(userName, email, password1, password2);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> requestMap) {
        String username = requestMap.get("username");
        String email = requestMap.get("email");
        String password1 = requestMap.get("password1");
        String password2 = requestMap.get("password2");

        // Call the signup method from the userService
        String result = userService.signUp(username, password1, password2, email);

        // Return an appropriate ResponseEntity
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/login")
    public String login(@RequestBody Map<String,Object> map) throws UnsupportedEncodingException {
        return userService.logIn(map.get("username").toString(), map.get("password").toString());
    }
}
