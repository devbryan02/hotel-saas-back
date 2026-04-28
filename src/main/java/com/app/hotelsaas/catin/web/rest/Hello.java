package com.app.hotelsaas.catin.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class Hello {

    @GetMapping
    public String helloWorld(@RequestParam(required = false) String name) {
        return "Hello " + name +", welcome to Hotel SaaS";
    }

}
