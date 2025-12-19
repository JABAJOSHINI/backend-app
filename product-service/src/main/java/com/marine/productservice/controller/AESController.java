
package com.marine.productservice.controller;

import com.marine.productservice.service.AESServiceImpl;
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
    public  void generatePdfReport() throws Exception {
        String input = "Hello, world!";
        String key = "your-encryption-key";
        System.out.println("request ");
        /*  return new Response();*/
        String encryptedString = aesService.encrypt(input,key) ;
        System.out.println(encryptedString);
        //aesService.encrypt( input,key);

    }
    @PostMapping("/decrypt")
    public  void decrypt() throws Exception {
        String input = "aGanYNw/wutK/5wcrAikCA==";
        String key = "your-encryption-key";
        System.out.println("request ");
        /*  return new Response();*/
        String encryptedString = aesService.decrypt(input, key) ;
        System.out.println(encryptedString);
        //aesService.encrypt( input,key);

    }
}
