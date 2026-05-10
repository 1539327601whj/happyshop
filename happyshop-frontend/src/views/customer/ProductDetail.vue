<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductDetail } from '../../api/product'
import { addToCart } from '../../api/cart'
import { addFavorite, removeFavorite, checkFavorite } from '../../api/favorite'
import { getProductReviews } from '../../api/review'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const product = ref({})
const reviews = ref([])
const isFavorite = ref(false)
const quantity = ref(1)
const selectedSku = ref(null)

onMounted(async () => {
  const id = route.params.id
  await loadProduct(id)
  loadReviews(id)
  if (userStore.isLoggedIn()) {
    checkFav(id)
  }
})

async function loadProduct(id) {
  try {
    const res = await getProductDetail(id)
    product.value = res.data || {}
  } catch (e) { /* ignored */ }
}

async function loadReviews(id) {
  try {
    const res = await getProductReviews(id, { current: 1, size: 5 })
    reviews.value = res.data?.records || []
  } catch (e) { /* ignored */ }
}

async function checkFav(id) {
  try {
    const res = await checkFavorite(id)
    isFavorite.value = res.data
  } catch (e) { /* ignored */ }
}

async function toggleFavorite() {
  if (!userStore.isLoggedIn()) {
    router.push('/login')
    return
  }
  try {
    if (isFavorite.value) {
      await removeFavorite(product.value.id)
      isFavorite.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite(product.value.id)
      isFavorite.value = true
      ElMessage.success('已收藏')
    }
  } catch (e) { /* ignored */ }
}

async function handleAddCart() {
  if (!userStore.isLoggedIn()) {
    router.push('/login')
    return
  }
  try {
    await addToCart({
      productId: product.value.id,
      skuId: selectedSku.value?.id,
      quantity: quantity.value
    })
    ElMessage.success('已加入购物车')
  } catch (e) { /* ignored */ }
}

function handleBuyNow() {
  if (!userStore.isLoggedIn()) {
    router.push('/login')
    return
  }
  handleAddCart()
  router.push('/cart')
}
</script>

<template>
  <div class="detail-page">
    <el-card class="detail-card">
      <div class="detail-top">
        <!-- 商品图片 -->
        <div class="product-images">
          <el-image :src="product.mainImage" fit="contain" style="width:100%;height:400px">
            <template #error>
              <div class="image-placeholder"><el-icon size="64"><Picture /></el-icon></div>
            </template>
          </el-image>
        </div>

        <!-- 商品信息 -->
        <div class="product-info">
          <h1>{{ product.name }}</h1>
          <p class="subtitle" v-if="product.subtitle">{{ product.subtitle }}</p>
          <div class="price-section">
            <span class="price">¥{{ product.price }}</span>
            <span v-if="product.marketPrice" class="market-price">¥{{ product.marketPrice }}</span>
          </div>
          <div class="info-row">
            <span>销量：{{ product.sales || 0 }}</span>
            <span>库存：{{ product.stock || 0 }}</span>
          </div>

          <!-- 规格选择 -->
          <div v-if="product.skus?.length" class="sku-section">
            <div v-for="sku in product.skus" :key="sku.id"
              :class="['sku-tag', { active: selectedSku?.id === sku.id }]"
              @click="selectedSku = sku">
              {{ sku.specName }}: {{ sku.specValue }}
            </div>
          </div>

          <!-- 数量 -->
          <div class="quantity-section">
            <span>数量：</span>
            <el-input-number v-model="quantity" :min="1" :max="product.stock || 99" />
          </div>

          <!-- 操作按钮 -->
          <div class="actions">
            <el-button type="primary" size="large" @click="handleAddCart">
              <el-icon><ShoppingCart /></el-icon> 加入购物车
            </el-button>
            <el-button type="danger" size="large" @click="handleBuyNow">立即购买</el-button>
            <el-button size="large" @click="toggleFavorite">
              <el-icon :color="isFavorite ? '#ff4400' : ''"><Star /></el-icon>
              {{ isFavorite ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 商品详情 -->
      <div class="detail-desc">
        <h3>商品详情</h3>
        <div v-html="product.description || '暂无详情'" />
      </div>
    </el-card>

    <!-- 评价 -->
    <el-card class="review-card">
      <template #header>
        <span>商品评价（{{ reviews.length }}）</span>
      </template>
      <div v-if="reviews.length === 0">
        <el-empty description="暂无评价" />
      </div>
      <div v-for="review in reviews" :key="review.id" class="review-item">
        <div class="review-header">
          <el-avatar :size="32">{{ review.isAnonymous ? '匿' : (review.nickname?.[0] || 'U') }}</el-avatar>
          <span class="review-user">{{ review.isAnonymous ? '匿名用户' : review.nickname }}</span>
          <el-rate v-model="review.rating" disabled size="small" />
          <span class="review-time">{{ review.createTime }}</span>
        </div>
        <p class="review-content">{{ review.content }}</p>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.detail-top {
  display: flex;
  gap: 40px;
}
.product-images {
  flex: 0 0 400px;
}
.image-placeholder {
  width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  color: #ccc;
  border-radius: 8px;
}
.product-info {
  flex: 1;
}
.product-info h1 {
  font-size: 22px;
  color: #333;
  margin-bottom: 8px;
}
.subtitle {
  color: #666;
  margin-bottom: 16px;
}
.price-section {
  background: #fff5f0;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}
.price {
  font-size: 28px;
  color: #ff4400;
  font-weight: bold;
}
.market-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
  margin-left: 12px;
}
.info-row {
  display: flex;
  gap: 24px;
  color: #666;
  margin-bottom: 16px;
}
.sku-section {
  margin-bottom: 16px;
}
.sku-tag {
  display: inline-block;
  padding: 6px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin: 0 8px 8px 0;
  cursor: pointer;
  transition: all 0.3s;
}
.sku-tag.active {
  border-color: #ff4400;
  color: #ff4400;
  background: #fff5f0;
}
.quantity-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}
.actions {
  display: flex;
  gap: 12px;
}
.detail-desc {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}
.detail-desc h3 {
  margin-bottom: 16px;
  font-size: 18px;
}
.review-card {
  margin-top: 24px;
}
.review-item {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}
.review-item:last-child {
  border-bottom: none;
}
.review-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.review-user {
  font-size: 14px;
  color: #333;
}
.review-time {
  font-size: 12px;
  color: #999;
  margin-left: auto;
}
.review-content {
  color: #666;
  line-height: 1.6;
}
</style>
