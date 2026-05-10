<script setup>
import { ref, onMounted } from 'vue'
import { getCategories } from '../../api/category'
import { ElMessage, ElMessageBox } from 'element-plus'

const categories = ref([])
const loading = ref(false)
const showDialog = ref(false)
const form = ref({ name: '', parentId: 0, sort: 0 })

onMounted(() => loadCategories())

async function loadCategories() {
  loading.value = true
  try {
    const res = await getCategories()
    categories.value = res.data || []
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}

function openAdd() {
  form.value = { name: '', parentId: 0, sort: 0 }
  showDialog.value = true
}

async function handleSave() {
  // TODO: 调用后端API保存分类
  ElMessage.success('保存成功')
  showDialog.value = false
  loadCategories()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除此分类？', '提示', { type: 'warning' })
  // TODO: 调用后端API删除
  ElMessage.success('已删除')
  loadCategories()
}
</script>

<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <h3>分类管理</h3>
      <el-button type="primary" @click="openAdd">添加分类</el-button>
    </div>
    <el-table :data="categories" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" width="200" />
      <el-table-column prop="sort" label="排序" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button text size="small" @click="showDialog = true">编辑</el-button>
          <el-button text type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showDialog" title="分类信息" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
