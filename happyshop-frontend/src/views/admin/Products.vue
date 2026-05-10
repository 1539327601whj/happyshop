<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const products = ref([])
const total = ref(0)
const loading = ref(false)

onMounted(() => loadProducts())

async function loadProducts() {
  loading.value = true
  // TODO: 调用后端API
  loading.value = false
}

async function handleAudit(row, status) {
  // TODO: 调用审核API
  ElMessage.success(status === 1 ? '审核通过' : '审核拒绝')
  loadProducts()
}
</script>

<template>
  <div>
    <h3 style="margin-bottom:16px">商品审核</h3>
    <el-table :data="products" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" min-width="200" />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }"><span style="color:#ff4400">¥{{ row.price }}</span></template>
      </el-table-column>
      <el-table-column prop="shopName" label="所属商家" width="150" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'">
            {{ row.status === 1 ? '已上架' : row.status === 2 ? '待审核' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <template v-if="row.status === 2">
            <el-button text type="success" size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button text type="danger" size="small" @click="handleAudit(row, 0)">拒绝</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && products.length === 0" description="暂无数据" />
  </div>
</template>
