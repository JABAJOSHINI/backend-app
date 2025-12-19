package com.marine.productservice.util;

import com.marine.productservice.entity.Products;
import com.marine.productservice.model.ProductModel;
import org.springframework.stereotype.Service;

@Service
public class DaoToDto {
    public ProductModel getProductFromDescOrSku(Products products) {
        ProductModel model = new ProductModel();
        model.setId(products.getId());
        model.setName(products.getName());
        model.setDescription(products.getDescription());
        model.setImage(products.getImage());
        model.setSku(products.getSku());
        model.setPrice(products.getPrice());
        model.setEquipment(products.getEquipment());
        model.setMake(products.getMake());
        model.setModel(products.getModel());
        model.setPartNumber(products.getPartNumber());
        model.setSpecification(products.getSpecification());
        model.setCoo(products.getCoo());
        model.setHazodus(products.getHazodus());
        if(products.getCategory() != null){
        model.setCategory(products.getCategory().getCategoryName());}
        if(products.getInventory() != null){
        model.setQuantity(products.getInventory().getQuantity());}
        return model;
    }
}
