package com.productmgmt.service.impl;

import com.productmgmt.dto.ProductDto;
import com.productmgmt.dto.ProductResponseDto;
import com.productmgmt.model.Product;
import com.productmgmt.repository.ProductRepository;
import com.productmgmt.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Boolean saveProduct(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        Product saveProduct = productRepository.save(product);
        return !ObjectUtils.isEmpty(saveProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = productList.stream().map(product -> mapper.map(product, ProductDto.class)).toList();
        return productDtoList;
    }

    @Override
    public ProductDto getProductById(Integer id) {
        Optional<Product> productById = productRepository.findById(id);
        if(productById.isPresent()) {
            Product product = productById.get();
            ProductDto productDto = mapper.map(product, ProductDto.class);
            return productDto;
        }
        return null;
    }

    @Override
    public Boolean deleteProduct(Integer id) {
        Optional<Product> productById = productRepository.findById(id);
        if(productById.isPresent()) {
            Product product = productById.get();
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public ProductResponseDto getProductsPagination(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        List<Product> products = page.getContent();

        List<ProductDto> productDtos = products.stream().map(product -> mapper.map(product, ProductDto.class)).toList();

        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        boolean first = page.isFirst();
        boolean last = page.isLast();

        ProductResponseDto productResponse = ProductResponseDto.builder().products(productDtos).totalElements(totalElements).totalPages(totalPages).isFirst(first).isLast(last).pageNo(pageNo).pageSize(pageSize).build();
        return productResponse;
    }

}
