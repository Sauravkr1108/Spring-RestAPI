package com.productmgmt.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponseDto {

    private List<ProductDto> products;

    private long totalElements;

    private int totalPages;

    private Boolean isFirst;

    private Boolean isLast;

    private int pageNo;

    private int pageSize;

}
