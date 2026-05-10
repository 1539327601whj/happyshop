package com.happyshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happyshop.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
