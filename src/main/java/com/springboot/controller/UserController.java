package com.springboot.controller;

import com.springboot.dao.ContactDao;
import com.springboot.dao.UserDao;
import com.springboot.entities.Contact;
import com.springboot.entities.User;
import com.springboot.helper.UserErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContactDao contactDao;

    @GetMapping("")
    public String base(Model model) {
        model.addAttribute("title", "Base");

        model.addAttribute("user", new User());
        return "user/base";
    }

    @ModelAttribute
    public void commonAttributes(Model model, Principal principal) {
        String email = principal.getName();
        System.out.println("EMAIL -> " + email);

        User user = userDao.getUserByUserName(email);
        System.out.println("USER -> " + user);

        model.addAttribute("user", user);

    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "Dashboard");


        return "/user/dashboard";
    }

    @GetMapping("/add_contact")
    public String addContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        return "user/add-contact-form";
    }

    @PostMapping("/process_addContact")
    public String processAddContact(@ModelAttribute("contact") Contact contact, @RequestParam("profileImage") MultipartFile file,
                                    Principal principal, Model model, HttpSession session) {
        try {
            model.addAttribute("contact", new Contact());
            System.out.println("Data From Form -> " + contact);

//            String name = principal.getName();
            User userName = userDao.getUserByUserName(principal.getName());
            contact.setUser(userName);

//            userName.getContacts().add(contact);

//            List<Contact> contacts = List.of(contact);
//            userName.setContacts(contacts);

            if (file.isEmpty()) {
                System.out.println("ProfileImage : none");
                contact.setProfile("default_profile.png");
            } else {
//                File resource = new ClassPathResource("/static/images").getFile();
                String projectImagePath = "C:\\Users\\Psiphon\\IdeaProjects\\Spring Boot - YouTube\\contactmanager-project\\src\\main\\resources\\static\\images\\user-images";

                String fileName = file.getOriginalFilename();

                //Original File name String
                int end = fileName.indexOf(".");
                String originalFileName = "";
                if (end != -1) {
                    originalFileName = fileName.substring(0, end);
                }

                String substring = fileName.substring(fileName.indexOf(".") + 1);
                System.out.println(substring);
                System.out.println("Original File Name : " + originalFileName);

                Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");

                Path path = Paths.get(projectImagePath + File.separator + originalFileName + "_" + formatter.format(new Date()) + "." + substring);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                contact.setProfile(originalFileName + "_" + formatter.format(new Date()) + "." + substring);
                System.out.println("Profile saved");

            }

//            User save = userDao.save(userName);
            Contact save = contactDao.save(contact);
            System.out.println("After Insert DB : " + save);
            session.setAttribute("message", new UserErrors("Saved successfully", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new UserErrors("Something Went Wrong", "alert-danger"));
        }


        return "user/add-contact-form";

    }


    @GetMapping("/view_contacts")
    public String showViewContacts(Model model, Principal principal) {
        model.addAttribute("title", "View Contacts");

        String userName = principal.getName();
        User userByUserName = userDao.getUserByUserName(userName);
        List<Contact> addContacts = contactDao.findContactByUser(userByUserName.getId());

//        List<Contact> addContacts = userByUserName.getContacts();

//        int userNameId = userByUserName.getId();
//
//        List<Contact> contacts = contactDao.findAll();
//
//        System.out.println("userNameID -> " + userNameId);
//        List<Contact> addContacts = new ArrayList<>();
//
//        for (Contact contact : contacts) {
//            if (userNameId == contact.getUser().getId()) {
//                System.out.println("contactUserID -> " + contact.getUser().getId());
//                addContacts.add(contact);
//            }
//
//        }

        addContacts.forEach(profileImg->{
            System.out.println(profileImg.getProfile());
        });
        model.addAttribute("contact", addContacts);
        return "/user/view-contacts";

    }

    @GetMapping("/view_page_update/{id}")
    public String showViewUpdate(@PathVariable("id") int id, Model model) {
        Optional<Contact> byId = contactDao.findById(id);
        if (byId.isPresent()) {
            Contact contact = byId.get();
            int cid = contact.getCid();
            System.out.println("Contact ID -> " + cid);

            model.addAttribute("contact", contact);
        }

        return "/user/add-contact-form";


    }

    @GetMapping("/view_page_delete/{id}")
    public String showViewDelete(@PathVariable("id") Integer id) {
        Optional<Contact> byId = contactDao.findById(id);
        if (byId.isPresent()) {
            Contact contact = byId.get();
            contact.setUser(null);
            contactDao.delete(contact);
        }


        return "redirect:/user/view_contacts";
    }


}
