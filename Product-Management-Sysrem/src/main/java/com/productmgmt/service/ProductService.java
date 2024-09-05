package com.productmgmt.service;

import com.productmgmt.dto.ProductDto;
import com.productmgmt.dto.ProductResponseDto;
import com.productmgmt.model.Product;

import java.util.List;

public interface ProductService {

    Boolean saveProduct(ProductDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Integer id);

    Boolean deleteProduct(Integer id);

    ProductResponseDto getProductsPagination(int pageNo, int pageSize, String sortBy, String sortDir);
}
