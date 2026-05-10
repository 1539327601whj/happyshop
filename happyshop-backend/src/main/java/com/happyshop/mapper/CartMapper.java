package com.happyshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happyshop.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
