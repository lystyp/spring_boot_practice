package com.example.spring_boot_pracrice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HelloWorld {

    @GetMapping("/hi")
    public String helloWorld() {
        return "hello";
    }
}
