package com.marine.productservice.service;

import com.marine.productservice.DTO.CategoryDto;
import com.marine.productservice.DTO.CountryDto;
import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.entity.CountryEntity;
import com.marine.productservice.entity.Products;
import com.marine.productservice.repository.CountryRepository;
import com.marine.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryDto CountryDto;
//    public List<CountryDto> getAllCountries() {
//        List<CountryEntity> countryList = countryRepository.findAll();
//        List<CountryDto> dtoList = getCountriesDto(countryList);
//        return dtoList;
//    }

//    private List<CountryDto> getCountriesDto(List<CountryEntity> countryList) {
//        List<CountryDto> dtoList = new ArrayList<>();
//        for (CountryEntity country : countryList) {
//            CountryDto countryDto = new CountryDto();  // Create a new instance in each iteration
//            countryDto.setId(country.getId());
//            countryDto.setName(country.getName());
//            countryDto.setCode(country.getCode());
//            countryDto.setDialCode(country.getDialCode());
//            dtoList.add(countryDto);
//        }
//        return dtoList;
//    }
}
