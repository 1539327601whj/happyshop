<script setup>
import { ref, onMounted } from 'vue'

const stats = ref({
  totalProducts: 0,
  totalOrders: 0,
  totalRevenue: 0,
  pendingOrders: 0
})
</script>

<template>
  <div>
    <h3 style="margin-bottom:20px">数据概览</h3>
    <el-row :gutter="20">
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
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="总收入(元)" :value="stats.totalRevenue" :precision="2" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="待处理订单" :value="stats.pendingOrders" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
