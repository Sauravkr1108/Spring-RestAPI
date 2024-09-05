package com.productmgmt.controller;

import com.productmgmt.dto.ProductDto;
import com.productmgmt.dto.ProductResponseDto;
import com.productmgmt.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home()
    {
        return "REST API project following industry level standards.";
    }

    @PostMapping("/save-product")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductDto productDto) {

        try{
            if(!productService.saveProduct(productDto))
                return new ResponseEntity<>("Product not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("e.getMessage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Product saved", HttpStatus.CREATED);
    }

    @GetMapping("/get-products")
    public ResponseEntity<?> getProducts() {

        List<ProductDto> productDtoList = null;
        try{
            productDtoList = productService.getAllProducts();
            if(CollectionUtils.isEmpty(productDtoList))
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("e.getMessage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {

        ProductDto productDto = null;
        try{
            productDto = productService.getProductById(id);
            if(ObjectUtils.isEmpty(productDto))
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("e.getMessage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {

        try{
            if(!productService.deleteProduct(id))
                return new ResponseEntity<>("Product not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("e.getMessage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }

    @GetMapping("/page-products")
    public ResponseEntity<?> getProductPagination(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "2") int pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {

        ProductResponseDto productResponse = null;
        try{
            productResponse = productService.getProductsPagination(pageNo, pageSize, sortBy, sortDir);
            if(ObjectUtils.isEmpty(productResponse))
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("e.getMessage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


}
