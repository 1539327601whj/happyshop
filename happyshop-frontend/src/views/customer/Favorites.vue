<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFavorites, removeFavorite } from '../../api/favorite'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const favorites = ref([])
const total = ref(0)
const loading = ref(false)

onMounted(() => loadFavorites())

async function loadFavorites() {
  loading.value = true
  try {
    const res = await getFavorites({ current: 1, size: 20 })
    favorites.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}

async function handleRemove(productId) {
  await ElMessageBox.confirm('确定取消收藏？', '提示', { type: 'warning' })
  try {
    await removeFavorite(productId)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch (e) { /* ignored */ }
}
</script>

<template>
  <div class="favorites-page">
    <el-card>
      <template #header><h3>我的收藏</h3></template>
      <div v-loading="loading">
        <div v-if="favorites.length === 0">
          <el-empty description="暂无收藏" />
        </div>
        <div class="product-grid">
          <div v-for="item in favorites" :key="item.id" class="product-card">
            <el-image :src="item.mainImage" fit="cover" style="width:100%;height:200px;cursor:pointer"
              @click="router.push(`/products/${item.productId}`)">
              <template #error>
                <div style="width:100%;height:200px;display:flex;align-items:center;justify-content:center;background:#f5f5f5;color:#ccc">
                  <el-icon size="48"><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="product-info">
              <h4 @click="router.push(`/products/${item.productId}`)">{{ item.productName }}</h4>
              <div class="bottom">
                <span class="price">¥{{ item.price }}</span>
                <el-button text type="danger" size="small" @click="handleRemove(item.productId)">取消收藏</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}
.product-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}
.product-info {
  padding: 12px;
}
.product-info h4 {
  font-size: 14px;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 8px;
}
.product-info h4:hover {
  color: #ff4400;
}
.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  color: #ff4400;
  font-weight: bold;
  font-size: 16px;
}
</style>
