package com.example.appcommons.controller;

import com.example.appcommons.Service.AESServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AESController {

    @Autowired
    AESServiceImpl aesService;
    @PostMapping("/aes")
    public  void generatePdfReport(@RequestBody String input, @RequestBody String key) throws Exception {

        System.out.println("request ");
        /*  return new Response();*/
        aesService.encrypt( input,key);
    }
}
