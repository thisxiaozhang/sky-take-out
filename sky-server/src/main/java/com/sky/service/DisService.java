package com.sky.service;

import com.sky.dto.DishDTO;

public interface DisService {
    /**
     * 新增菜品及口味
     * @param dishDTO
     */

    public void saveWithFlavor(DishDTO dishDTO);
}
