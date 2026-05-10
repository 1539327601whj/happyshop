<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getProducts } from '../../api/product'
import { getCategories } from '../../api/category'

const router = useRouter()
const route = useRoute()

const products = ref([])
const categories = ref([])
const total = ref(0)
const loading = ref(false)

const queryParams = ref({
  current: 1,
  size: 12,
  keyword: route.query.keyword || '',
  categoryId: route.query.categoryId || null,
  sort: 'default'
})

onMounted(() => {
  loadCategories()
  loadProducts()
})

watch(() => route.query, (query) => {
  queryParams.value.keyword = query.keyword || ''
  queryParams.value.categoryId = query.categoryId || null
  queryParams.value.current = 1
  loadProducts()
})

async function loadCategories() {
  try {
    const res = await getCategories()
    categories.value = res.data || []
  } catch (e) { /* ignored */ }
}

async function loadProducts() {
  loading.value = true
  try {
    const res = await getProducts(queryParams.value)
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}

function handleSearch() {
  queryParams.value.current = 1
  loadProducts()
}

function handleCategoryChange(catId) {
  queryParams.value.categoryId = catId
  queryParams.value.current = 1
  loadProducts()
}

function handlePageChange(page) {
  queryParams.value.current = page
  loadProducts()
}

function goToDetail(id) {
  router.push(`/products/${id}`)
}
</script>

<template>
  <div class="products-page">
    <!-- 搜索和筛选 -->
    <div class="filter-bar">
      <el-input v-model="queryParams.keyword" placeholder="搜索商品" @keyup.enter="handleSearch" clearable style="width:300px">
        <template #append>
          <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
        </template>
      </el-input>
      <el-select v-model="queryParams.categoryId" placeholder="全部分类" clearable @change="handleCategoryChange" style="width:160px">
        <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
      </el-select>
      <el-select v-model="queryParams.sort" @change="handleSearch" style="width:140px">
        <el-option label="默认排序" value="default" />
        <el-option label="价格从低到高" value="price_asc" />
        <el-option label="价格从高到低" value="price_desc" />
        <el-option label="销量优先" value="sales_desc" />
      </el-select>
    </div>

    <!-- 商品列表 -->
    <div v-loading="loading" class="product-grid">
      <div v-for="product in products" :key="product.id" class="product-card" @click="goToDetail(product.id)">
        <el-image :src="product.mainImage" fit="cover" style="width:100%;height:200px">
          <template #error>
            <div class="image-placeholder"><el-icon size="48"><Picture /></el-icon></div>
          </template>
        </el-image>
        <div class="product-info">
          <h4>{{ product.name }}</h4>
          <div class="price-row">
            <span class="price">¥{{ product.price }}</span>
            <span v-if="product.marketPrice" class="market-price">¥{{ product.marketPrice }}</span>
          </div>
          <div class="sales">已售 {{ product.sales || 0 }} 件</div>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && products.length === 0" description="暂无商品" />

    <!-- 分页 -->
    <div class="pagination" v-if="total > queryParams.size">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="queryParams.size"
        :current-page="queryParams.current"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped>
.filter-bar {
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}
.product-card {
  background: #fff;
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
.product-info h4 {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 8px;
}
.price-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 4px;
}
.price {
  font-size: 18px;
  color: #ff4400;
  font-weight: bold;
}
.market-price {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
}
.sales {
  font-size: 12px;
  color: #999;
}
.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
