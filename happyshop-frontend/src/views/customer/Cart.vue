<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../../stores/cart'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()

onMounted(() => {
  cartStore.fetchCart()
})

function handleQuantityChange(item) {
  cartStore.updateItem(item.id, { quantity: item.quantity, checked: item.checked })
}

function handleCheckChange(item) {
  cartStore.updateItem(item.id, { quantity: item.quantity, checked: item.checked ? 1 : 0 })
}

async function handleRemove(item) {
  await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', { type: 'warning' })
  await cartStore.removeItem(item.id)
}

function handleCheckout() {
  if (cartStore.totalCount === 0) return
  router.push('/checkout')
}
</script>

<template>
  <div class="cart-page">
    <el-card>
      <template #header>
        <h3>我的购物车</h3>
      </template>

      <div v-if="cartStore.cartItems.length === 0">
        <el-empty description="购物车是空的">
          <el-button type="primary" @click="router.push('/products')">去购物</el-button>
        </el-empty>
      </div>

      <template v-else>
        <el-table :data="cartStore.cartItems" style="width: 100%">
          <el-table-column width="50">
            <template #default="{ row }">
              <el-checkbox v-model="row.checked" :true-value="1" :false-value="0" @change="handleCheckChange(row)" />
            </template>
          </el-table-column>
          <el-table-column label="商品" min-width="300">
            <template #default="{ row }">
              <div class="product-cell">
                <el-image :src="row.productImage" fit="cover" style="width:80px;height:80px;border-radius:4px" />
                <div>
                  <p class="product-name" @click="router.push(`/products/${row.productId}`)">{{ row.productName }}</p>
                  <p v-if="row.skuSpec" class="sku-spec">{{ row.skuSpec }}</p>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template #default="{ row }">
              <span class="price">¥{{ row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="160">
            <template #default="{ row }">
              <el-input-number v-model="row.quantity" :min="1" :max="99" size="small" @change="handleQuantityChange(row)" />
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120">
            <template #default="{ row }">
              <span class="price">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ row }">
              <el-button type="danger" text @click="handleRemove(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="cart-footer">
          <div class="total">
            共 <strong>{{ cartStore.totalCount }}</strong> 件商品，合计：
            <span class="total-price">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
          </div>
          <el-button type="danger" size="large" @click="handleCheckout">去结算</el-button>
        </div>
      </template>
    </el-card>
  </div>
</template>

<style scoped>
.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}
.product-name {
  font-size: 14px;
  color: #333;
  cursor: pointer;
}
.product-name:hover {
  color: #ff4400;
}
.sku-spec {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
.price {
  color: #ff4400;
  font-weight: bold;
}
.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 24px;
  padding: 20px 0;
  border-top: 1px solid #eee;
  margin-top: 16px;
}
.total {
  font-size: 16px;
  color: #333;
}
.total-price {
  font-size: 24px;
  color: #ff4400;
  font-weight: bold;
}
</style>
