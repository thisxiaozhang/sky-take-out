package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    PageResult PageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
