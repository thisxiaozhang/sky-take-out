package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据对应的菜品id查询套餐id
     * @param disIds
     * @return
     */
    //select setmeal id from setmail dish where dish_id in (1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> disIds);
}
