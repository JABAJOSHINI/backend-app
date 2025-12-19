package com.marine.productservice.paginaion;

import java.util.List;

public record PaginationResponse<T>(List<T> content, Long totalElements, Integer totalPages, Integer currentPage){
}
