package com.springboot.controller;

import com.springboot.dao.UserDao;
import com.springboot.entities.User;
import com.springboot.helper.UserErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("")
    public String base(Model model) {
        model.addAttribute("title", "Base");
        return "base";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");

        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");

        return "about";
    }

    @GetMapping("/signin")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("user", new User());

        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping("/process_signup")
    public String processSignup(@Valid @ModelAttribute User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "signup";
        }
        System.out.println(user);
        System.out.println("agreement: " + agreement);

        try {

            user.setRole("ROLE_USER");
            user.setStatus(true);
            user.setProfile("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User result = userDao.save(user);

            model.addAttribute("user", new User());
            session.setAttribute("message", new UserErrors("Register success", "alert-success"));

            return "signup";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new UserErrors("Something went wrong", "alert-danger"));
            return "signup";
        }
    }


}
