<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrderDetail, payOrder, cancelOrder, confirmReceive } from '../../api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const orderItems = ref([])

const statusMap = {
  0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消', 5: '退款中'
}

onMounted(async () => {
  try {
    const res = await getOrderDetail(route.params.id)
    order.value = res.data
    orderItems.value = res.data?.items || []
  } catch (e) { /* ignored */ }
})

async function handlePay() {
  await ElMessageBox.confirm('确认支付此订单？', '支付确认')
  try {
    await payOrder(order.value.id)
    ElMessage.success('支付成功')
    order.value.status = 1
  } catch (e) { /* ignored */ }
}

async function handleCancel() {
  await ElMessageBox.confirm('确定取消此订单？', '提示', { type: 'warning' })
  try {
    await cancelOrder(order.value.id)
    ElMessage.success('已取消')
    order.value.status = 4
  } catch (e) { /* ignored */ }
}

async function handleConfirm() {
  await ElMessageBox.confirm('确认已收到商品？', '确认收货')
  try {
    await confirmReceive(order.value.id)
    ElMessage.success('已确认收货')
    order.value.status = 3
  } catch (e) { /* ignored */ }
}
</script>

<template>
  <div class="order-detail" v-if="order">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>订单详情</h3>
          <el-tag>{{ statusMap[order.status] }}</el-tag>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ order.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ order.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ order.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="订单备注" :span="2">{{ order.remark || '无' }}</el-descriptions-item>
      </el-descriptions>

      <h4 style="margin:24px 0 12px">商品明细</h4>
      <el-table :data="orderItems" style="width:100%">
        <el-table-column label="商品" min-width="250">
          <template #default="{ row }">
            <div style="display:flex;align-items:center;gap:12px">
              <el-image :src="row.productImage" fit="cover" style="width:60px;height:60px;border-radius:4px" />
              <div>
                <p>{{ row.productName }}</p>
                <p v-if="row.skuSpec" style="font-size:12px;color:#999">{{ row.skuSpec }}</p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120">
          <template #default="{ row }"><span class="price">¥{{ row.price }}</span></template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column label="小计" width="120">
          <template #default="{ row }"><span class="price">¥{{ row.totalAmount }}</span></template>
        </el-table-column>
      </el-table>

      <div class="order-summary">
        <p>商品合计：<span class="price">¥{{ order.totalAmount }}</span></p>
        <p v-if="order.discountAmount > 0">优惠：-¥{{ order.discountAmount }}</p>
        <p>运费：¥{{ order.freightAmount || '0.00' }}</p>
        <p class="pay-amount">实付：<span class="price big">¥{{ order.payAmount }}</span></p>
      </div>

      <div class="actions">
        <el-button v-if="order.status === 0" type="danger" @click="handlePay">立即付款</el-button>
        <el-button v-if="order.status === 0" @click="handleCancel">取消订单</el-button>
        <el-button v-if="order.status === 2" type="primary" @click="handleConfirm">确认收货</el-button>
        <el-button @click="router.push('/orders')">返回列表</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  color: #ff4400;
  font-weight: bold;
}
.price.big {
  font-size: 20px;
}
.order-summary {
  text-align: right;
  padding: 20px 0;
  border-top: 1px solid #eee;
  margin-top: 16px;
}
.order-summary p {
  margin-bottom: 8px;
  color: #666;
}
.pay-amount {
  font-size: 16px;
  color: #333;
}
.actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
}
</style>
