package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.AuthenticationServiceJPA;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private User loggedUser;

    private boolean register = false;

    @Autowired
    private AuthenticationServiceJPA authenticationService;

    @RequestMapping("/login")
    public String login(String username, String password){
        if(authenticationService.isRegistered(new User(username,password))){
            loggedUser = new User(username,password);
            return "redirect:/Taptiles";
        }
        return "redirect:/new?level=1";
    }

    @RequestMapping("/reg")
    public String regButton(){
        register = true;
        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String username,String password){

        if(!authenticationService.register(new User(username,password))){
            return "redirect:/";
        }

        register = false;
        loggedUser = new User(username,password);

        return "redirect:/Taptiles/new?level=1";
    }


    @RequestMapping("/logout")
    public String logout(String login,String password){
        loggedUser = null;
        register = false;

        return "redirect:/";
    }

    public User getLoggedUser(){
        return loggedUser;
    }

    public boolean isLogged(){
        return loggedUser != null;
    }

    public boolean isRegister(){
        return register;
    }

    public void setRegisterFalse(){
        register = false;
    }

}
