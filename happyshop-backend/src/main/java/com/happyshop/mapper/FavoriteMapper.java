package com.happyshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happyshop.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
