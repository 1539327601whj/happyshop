<script setup>
import { ref, onMounted } from 'vue'

const users = ref([])
const total = ref(0)
const loading = ref(false)

onMounted(() => loadUsers())

async function loadUsers() {
  loading.value = true
  // TODO: 调用后端API获取用户列表
  loading.value = false
}
</script>

<template>
  <div>
    <h3 style="margin-bottom:16px">用户管理</h3>
    <el-table :data="users" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="nickname" label="昵称" width="150" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : row.role === 'MERCHANT' ? 'warning' : ''">
            {{ row.role === 'ADMIN' ? '管理员' : row.role === 'MERCHANT' ? '商家' : '用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button text size="small">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && users.length === 0" description="暂无数据" />
  </div>
</template>
