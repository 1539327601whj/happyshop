<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAddresses } from '../../api/address'
import { createOrder } from '../../api/order'
import { useCartStore } from '../../stores/cart'
import { ElMessage } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()
const addresses = ref([])
const selectedAddress = ref(null)
const remark = ref('')
const loading = ref(false)

onMounted(async () => {
  cartStore.fetchCart()
  try {
    const res = await getAddresses()
    addresses.value = res.data || []
    selectedAddress.value = addresses.value.find(a => a.isDefault)?.id || addresses.value[0]?.id
  } catch (e) { /* ignored */ }
})

async function handleSubmit() {
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  loading.value = true
  try {
    const checkedItems = cartStore.cartItems.filter(i => i.checked)
    await createOrder({
      addressId: selectedAddress.value,
      items: checkedItems.map(i => ({ productId: i.productId, skuId: i.skuId, quantity: i.quantity })),
      remark: remark.value
    })
    ElMessage.success('下单成功')
    router.push('/orders')
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}
</script>

<template>
  <div class="checkout-page">
    <el-card>
      <template #header><h3>确认订单</h3></template>

      <!-- 收货地址 -->
      <div class="section">
        <h4>收货地址</h4>
        <el-radio-group v-model="selectedAddress">
          <div v-for="addr in addresses" :key="addr.id" class="address-item">
            <el-radio :value="addr.id">
              <strong>{{ addr.receiverName }}</strong> {{ addr.receiverPhone }}
              <span>{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}</span>
            </el-radio>
          </div>
        </el-radio-group>
        <el-empty v-if="addresses.length === 0" description="暂无收货地址，请先添加" :image-size="60">
          <el-button type="primary" size="small" @click="router.push('/user')">去添加</el-button>
        </el-empty>
      </div>

      <!-- 商品清单 -->
      <div class="section">
        <h4>商品清单</h4>
        <el-table :data="cartStore.cartItems.filter(i => i.checked)" style="width:100%">
          <el-table-column label="商品" min-width="300">
            <template #default="{ row }">
              <div class="product-cell">
                <el-image :src="row.productImage" fit="cover" style="width:60px;height:60px;border-radius:4px" />
                <span>{{ row.productName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template #default="{ row }"><span class="price">¥{{ row.price }}</span></template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" />
          <el-table-column label="小计" width="120">
            <template #default="{ row }"><span class="price">¥{{ (row.price * row.quantity).toFixed(2) }}</span></template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 备注 -->
      <div class="section">
        <h4>订单备注</h4>
        <el-input v-model="remark" type="textarea" :rows="2" placeholder="选填" maxlength="200" show-word-limit />
      </div>

      <!-- 结算 -->
      <div class="checkout-footer">
        <div class="total">
          合计：<span class="total-price">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
        </div>
        <el-button type="danger" size="large" :loading="loading" @click="handleSubmit">提交订单</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.section {
  margin-bottom: 24px;
}
.section h4 {
  margin-bottom: 12px;
  font-size: 16px;
}
.address-item {
  padding: 8px 0;
}
.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}
.price {
  color: #ff4400;
  font-weight: bold;
}
.checkout-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 24px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
.total-price {
  font-size: 24px;
  color: #ff4400;
  font-weight: bold;
}
</style>
