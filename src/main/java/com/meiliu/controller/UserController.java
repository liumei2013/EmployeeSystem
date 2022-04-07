package com.meiliu.controller;

import com.meiliu.entity.User;
import com.meiliu.service.UserService;
import com.meiliu.util.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * safe logout
     */
    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/employee/lists";
    }


    /**
     * user login
     * @return
     */
    @RequestMapping("login")
    public String login(String username, String password, HttpSession session){
        log.debug("current login username: {}",username);
        log.debug("current login password: {}",password);
        try {
            User user = userService.login(username,password);
            //save user infor
            session.setAttribute("user",user);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login";//failed login and return to login page
        }
        return "redirect:/employee/lists";//after succussful register, redirect to all employee controller path
    }



    /**
     * user registration
     * @return
     */
    @RequestMapping("register")
    public String register(User user, String code, HttpSession session){
        log.debug("username: {}, realname: {}, password: {}, gender: {}", user.getUsername(),user.getRealname(),user.getGender(),user.getPassword());
        log.debug("input password:{}", code);

        //verify the input pwd and session code
        try {
            String sessionCode = session.getAttribute("code").toString();
            if(!sessionCode.equalsIgnoreCase(code)) throw new RuntimeException("Incorrect verified code!");
            userService.register(user);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return  "redirect:/register"; // failed registration
        }
        return "redirect:/login"; //successful registration
    }


    @RequestMapping("generateimageCode")
    public void generateImageCode(HttpSession session,HttpServletResponse response) throws IOException {
        //generate 4 random digits
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //save to session
        session.setAttribute("code",code);
        //generate img
        response.setContentType("image/png");
        ServletOutputStream os = response.getOutputStream();
        VerifyCodeUtils.outputImage(220,60,os,code);
        //respond img

    }

}
