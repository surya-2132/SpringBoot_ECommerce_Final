package com.fullstackproject.ecommerce.Service;

import com.fullstackproject.ecommerce.Model.User;
import com.fullstackproject.ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String getUserByName(String user){
        return userRepository.getUserByUserName(user).toString();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //since it's an signUp, Create a new User and set their attribute
    public String signUp(String userName, String email, String password1, String password2){
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassWord1(password1);
        user.setPassWord2(password2);

        User newUser = userRepository.save(user);
        return "{" +
                "\"message\":"+"Successfully created user\",\n"+
                "\"data\": "+newUser+",\n"+
                "}";
    }

    //if users exist, do logIn using his previous data(from all user details)
    public String logIn(String userName, String password) throws UnsupportedEncodingException {
        List<User> existingUser = userRepository.getUserByUserName(userName);

        //if no user found
        if(existingUser.isEmpty()){
            return "User Not Found";
            //if password didn't match(pwd1 != pwd2)
        } else if (!existingUser.get(0).getPassWord1().equals(password) &&
                    !existingUser.get(0).getPassWord2().equals(password)) {
            return "Incorrect Password";
        }//user and their pwd matched
        return "{\n" +
                "\"message\":"+"\" User Successfully Logged-in\",\n"+
                "\"data\": {\n"+" Name : "+existingUser.get(0).getUserName()+",\n"+
                "Email : "+existingUser.get(0).getEmail()+"\n"+
                "\"token\":\""+tokenService.createTokenAndEncrypt(existingUser.get(0).getId())+"\"" +
                "}";
    }
}
