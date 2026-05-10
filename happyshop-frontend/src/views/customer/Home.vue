<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getRecommendProducts } from '../../api/product'
import { getCategoryTree } from '../../api/category'

const router = useRouter()
const categories = ref([])
const recommendProducts = ref([])
const banners = ref([
  { id: 1, title: '限时特惠', subtitle: '全场低至5折', color: '#ff6b6b' },
  { id: 2, title: '新品上市', subtitle: '潮流新品抢先购', color: '#4ecdc4' },
  { id: 3, title: '会员专享', subtitle: '积分翻倍再送券', color: '#45b7d1' },
])

onMounted(async () => {
  try {
    const [catRes, prodRes] = await Promise.all([
      getCategoryTree().catch(() => ({ data: [] })),
      getRecommendProducts().catch(() => ({ data: [] }))
    ])
    categories.value = catRes.data || []
    recommendProducts.value = prodRes.data || []
  } catch (e) {
    // 静默处理
  }
})

function goToProducts(categoryId) {
  router.push({ path: '/products', query: { categoryId } })
}

function goToDetail(id) {
  router.push(`/products/${id}`)
}
</script>

<template>
  <div class="home">
    <!-- 轮播图 -->
    <el-carousel height="300px" class="banner">
      <el-carousel-item v-for="item in banners" :key="item.id">
        <div class="banner-item" :style="{ background: item.color }">
          <h2>{{ item.title }}</h2>
          <p>{{ item.subtitle }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 分类导航 -->
    <section class="section">
      <h3 class="section-title">商品分类</h3>
      <div class="category-grid">
        <div v-for="cat in categories" :key="cat.id" class="category-item" @click="goToProducts(cat.id)">
          <el-icon size="32" color="#ff4400"><Grid /></el-icon>
          <span>{{ cat.name }}</span>
        </div>
      </div>
    </section>

    <!-- 推荐商品 -->
    <section class="section">
      <h3 class="section-title">为你推荐</h3>
      <div class="product-grid">
        <div v-for="product in recommendProducts" :key="product.id" class="product-card" @click="goToDetail(product.id)">
          <div class="product-image">
            <el-image :src="product.mainImage" fit="cover" style="width:100%;height:200px">
              <template #error>
                <div class="image-placeholder">
                  <el-icon size="48"><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </div>
          <div class="product-info">
            <h4 class="product-name">{{ product.name }}</h4>
            <p class="product-price">¥{{ product.price }}</p>
          </div>
        </div>
      </div>
      <div v-if="recommendProducts.length === 0" class="empty-tip">
        <el-empty description="暂无推荐商品" />
      </div>
    </section>
  </div>
</template>

<style scoped>
.home {
  padding: 0;
}
.banner {
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 24px;
}
.banner-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
}
.banner-item h2 {
  font-size: 36px;
  margin-bottom: 12px;
}
.banner-item p {
  font-size: 18px;
}
.section {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 24px;
}
.section-title {
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
  border-left: 4px solid #ff4400;
  padding-left: 12px;
}
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
}
.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}
.category-item:hover {
  background: #fff5f0;
  color: #ff4400;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}
.product-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}
.product-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transform: translateY(-2px);
}
.image-placeholder {
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  color: #ccc;
}
.product-info {
  padding: 12px;
}
.product-name {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 8px;
}
.product-price {
  font-size: 18px;
  color: #ff4400;
  font-weight: bold;
}
.empty-tip {
  padding: 40px 0;
}
</style>
