package com.tuancd.demo1.controller;

import com.tuancd.demo1.repository.IRoomRepository;
import com.tuancd.demo1.repository.IRoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    public String showHomePage(Model model) {
        return "home";
    }
}