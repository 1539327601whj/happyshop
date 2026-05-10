<script setup>
import { ref, onMounted } from 'vue'
import { getProducts } from '../../api/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const products = ref([])
const total = ref(0)
const loading = ref(false)
const showDialog = ref(false)
const form = ref({
  name: '', categoryId: null, price: 0, marketPrice: 0, stock: 0, description: '', subtitle: ''
})

onMounted(() => loadProducts())

async function loadProducts() {
  loading.value = true
  try {
    const res = await getProducts({ current: 1, size: 20 })
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}

function openAdd() {
  form.value = { name: '', categoryId: null, price: 0, marketPrice: 0, stock: 0, description: '', subtitle: '' }
  showDialog.value = true
}

async function handleSave() {
  // TODO: 调用后端API保存商品
  ElMessage.success('保存成功')
  showDialog.value = false
  loadProducts()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除此商品？', '提示', { type: 'warning' })
  // TODO: 调用后端API删除
  ElMessage.success('已删除')
  loadProducts()
}
</script>

<template>
  <div>
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <h3>商品管理</h3>
      <el-button type="primary" @click="openAdd">添加商品</el-button>
    </div>

    <el-table :data="products" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" min-width="200" />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }"><span style="color:#ff4400">¥{{ row.price }}</span></template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="sales" label="销量" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'">
            {{ row.status === 1 ? '上架' : row.status === 2 ? '待审核' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button text size="small" @click="showDialog = true">编辑</el-button>
          <el-button text type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showDialog" title="商品信息" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="副标题">
          <el-input v-model="form.subtitle" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价">
          <el-input-number v-model="form.marketPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
