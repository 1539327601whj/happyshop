<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const promotions = ref([])
const total = ref(0)
const loading = ref(false)
const showDialog = ref(false)
const form = ref({
  name: '', type: 'FULL_REDUCE', discountValue: 0, minAmount: 0,
  startTime: '', endTime: '', description: ''
})

onMounted(() => loadPromotions())

async function loadPromotions() {
  loading.value = true
  // TODO: 调用后端API
  loading.value = false
}

function openAdd() {
  form.value = { name: '', type: 'FULL_REDUCE', discountValue: 0, minAmount: 0, startTime: '', endTime: '', description: '' }
  showDialog.value = true
}

async function handleSave() {
  // TODO: 调用后端API
  ElMessage.success('保存成功')
  showDialog.value = false
  loadPromotions()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除此活动？', '提示', { type: 'warning' })
  // TODO: 调用后端API
  ElMessage.success('已删除')
  loadPromotions()
}
</script>

<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <h3>促销活动</h3>
      <el-button type="primary" @click="openAdd">创建活动</el-button>
    </div>
    <el-table :data="promotions" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="活动名称" width="200" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          {{ row.type === 'FULL_REDUCE' ? '满减' : row.type === 'DISCOUNT' ? '折扣' : '优惠券' }}
        </template>
      </el-table-column>
      <el-table-column prop="discountValue" label="优惠值" width="120" />
      <el-table-column prop="startTime" label="开始时间" width="180" />
      <el-table-column prop="endTime" label="结束时间" width="180" />
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
    <el-empty v-if="!loading && promotions.length === 0" description="暂无活动" />

    <el-dialog v-model="showDialog" title="活动信息" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="活动名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="form.type">
            <el-option label="满减" value="FULL_REDUCE" />
            <el-option label="折扣" value="DISCOUNT" />
            <el-option label="优惠券" value="COUPON" />
          </el-select>
        </el-form-item>
        <el-form-item label="优惠值">
          <el-input-number v-model="form.discountValue" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="最低消费">
          <el-input-number v-model="form.minAmount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
