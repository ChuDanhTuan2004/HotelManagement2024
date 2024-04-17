package com.tuancd.demo1.controller;

import com.tuancd.demo1.dto.PaginateRequest;
import com.tuancd.demo1.dto.UserDTO;
import com.tuancd.demo1.dto.UserRequest;
import com.tuancd.demo1.model.Role;
import com.tuancd.demo1.model.User;
import com.tuancd.demo1.repository.IRoleRepository;
import com.tuancd.demo1.repository.IUserRepository;
import com.tuancd.demo1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    IRoleRepository roleRepository;

    @GetMapping("/listUser")
    public String showListUser(Model model,
                               @RequestParam(name = "username", required = false) String username,
                               @RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "role", required = false) Long role,
                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail(email);
        userDTO.setRole(role);

        PaginateRequest paginateRequest = new PaginateRequest(page, size);
        Page<User> users = userService.findAll(userDTO, paginateRequest);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());

        model.addAttribute("users", users.getContent());
        return "/user/listUser";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        UserRequest userRequest = new UserRequest();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("userRequest", userRequest);
        model.addAttribute("roles", roles);
        return "user/addUser";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {
        if (userRequest.getImage().isEmpty()) {
            result.addError(new FieldError("userRequest", "image", "The image file is required"));
        }

        if (result.hasErrors()) {
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "user/addUser";
        }

        //Save image file
        MultipartFile image = userRequest.getImage();
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setImage(storageFileName);
        user.setRole(userRequest.getRole());
        user.setCreateAt(createAt);

        userRepository.save(user);

        return "redirect:/users/listUser";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam Long id) {
        try {
            User user = userRepository.findById(id).get();
            model.addAttribute("user", user);
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);

            UserRequest userRequest = new UserRequest();
            userRequest.setUsername(user.getUsername());
            userRequest.setEmail(user.getEmail());
            userRequest.setPassword(user.getPassword());
            userRequest.setRole(user.getRole());

            model.addAttribute("userRequest", userRequest);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/users/listUser";
        }
        return "user/updateUser";
    }

    @PostMapping("/edit")
    public String edit(Model model, @RequestParam Long id,
                       @Valid @ModelAttribute UserRequest userRequest, BindingResult result) {
        try {
            User user = userRepository.findById(id).get();
            model.addAttribute("user", user);

            if (result.hasErrors()) {
                List<Role> roles = roleRepository.findAll();
                model.addAttribute("roles", roles);
                return "user/updateUser";
            }

            if (!userRequest.getImage().isEmpty()) {
                //delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + user.getImage());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception exception) {
                    System.out.println("Exception: " + exception.getMessage());
                }

                //save new image
                MultipartFile image = userRequest.getImage();
                Date createAt = new Date();
                String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                user.setImage(storageFileName);
            }

            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setRole(userRequest.getRole());
            user.setPassword(userRequest.getPassword());

            userRepository.save(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "redirect:/users/listUser";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        try {
            User user = userRepository.findById(id).get();

            //delete room image
            Path imagePath = Paths.get("public/images/" + user.getImage());

            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }

            //delete room
            userRepository.delete(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "redirect:/users/listUser";
    }
}