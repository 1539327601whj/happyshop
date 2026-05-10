package com.happyshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happyshop.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
