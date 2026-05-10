<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const merchants = ref([])
const total = ref(0)
const loading = ref(false)

onMounted(() => loadMerchants())

async function loadMerchants() {
  loading.value = true
  // TODO: 调用后端API获取商家列表
  loading.value = false
}

async function handleAudit(row, status) {
  // TODO: 调用审核API
  ElMessage.success(status === 1 ? '审核通过' : '审核拒绝')
  loadMerchants()
}
</script>

<template>
  <div>
    <h3 style="margin-bottom:16px">商家管理</h3>
    <el-table :data="merchants" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="shopName" label="店铺名称" width="200" />
      <el-table-column prop="contactName" label="联系人" width="120" />
      <el-table-column prop="contactPhone" label="联系电话" width="150" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'">
            {{ row.status === 1 ? '已通过' : row.status === 2 ? '已拒绝' : '待审核' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button text type="success" size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button text type="danger" size="small" @click="handleAudit(row, 2)">拒绝</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && merchants.length === 0" description="暂无数据" />
  </div>
</template>
