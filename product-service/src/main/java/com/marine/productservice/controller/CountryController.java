package com.marine.productservice.controller;

import com.marine.productservice.DTO.CountryDto;
import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CountryController {
    @Autowired
    private CountryService countryService;
//    @GetMapping("/country/list")
//    public List<CountryDto> getAllCountries() {
//        List<CountryDto> products = countryService.getAllCountries();
//        return products;
//    }
}
