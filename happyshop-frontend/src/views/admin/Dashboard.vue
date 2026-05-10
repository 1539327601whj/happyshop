<script setup>
import { ref } from 'vue'

const stats = ref({
  totalUsers: 0,
  totalMerchants: 0,
  totalProducts: 0,
  totalOrders: 0
})
</script>

<template>
  <div>
    <h3 style="margin-bottom:20px">系统概览</h3>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="用户总数" :value="stats.totalUsers" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="商家总数" :value="stats.totalMerchants" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="商品总数" :value="stats.totalProducts" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="订单总数" :value="stats.totalOrders" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
