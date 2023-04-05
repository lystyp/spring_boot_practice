package com.example.spring_boot_pracrice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateThreadController {

    @GetMapping("/thread")
    public String index(){
        return "thread";
    }


}
