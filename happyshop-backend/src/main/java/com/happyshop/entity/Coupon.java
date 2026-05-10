package com.happyshop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("coupon")
public class Coupon {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long promotionId;
    private Long userId;
    private String code;
    private String name;
    private String type;
    private BigDecimal value;
    private BigDecimal minAmount;
    private Integer status;
    private LocalDateTime usedTime;
    private LocalDateTime expireTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
