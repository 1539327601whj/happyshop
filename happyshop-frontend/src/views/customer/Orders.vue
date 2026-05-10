<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrders, payOrder, cancelOrder, confirmReceive } from '../../api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const orders = ref([])
const total = ref(0)
const currentTab = ref('all')
const loading = ref(false)

const statusMap = {
  0: { label: '待付款', color: '#ff4400' },
  1: { label: '待发货', color: '#e6a23c' },
  2: { label: '待收货', color: '#409eff' },
  3: { label: '已完成', color: '#67c23a' },
  4: { label: '已取消', color: '#909399' },
  5: { label: '退款中', color: '#f56c6c' },
}

onMounted(() => loadOrders())

async function loadOrders() {
  loading.value = true
  try {
    const params = { current: 1, size: 20 }
    if (currentTab.value !== 'all') params.status = currentTab.value
    const res = await getOrders(params)
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}

function handleTabChange() {
  loadOrders()
}

async function handlePay(order) {
  await ElMessageBox.confirm('确认支付此订单？', '支付确认')
  try {
    await payOrder(order.id)
    ElMessage.success('支付成功')
    loadOrders()
  } catch (e) { /* ignored */ }
}

async function handleCancel(order) {
  await ElMessageBox.confirm('确定取消此订单？', '提示', { type: 'warning' })
  try {
    await cancelOrder(order.id)
    ElMessage.success('已取消')
    loadOrders()
  } catch (e) { /* ignored */ }
}

async function handleConfirm(order) {
  await ElMessageBox.confirm('确认已收到商品？', '确认收货')
  try {
    await confirmReceive(order.id)
    ElMessage.success('已确认收货')
    loadOrders()
  } catch (e) { /* ignored */ }
}
</script>

<template>
  <div class="orders-page">
    <el-card>
      <template #header><h3>我的订单</h3></template>

      <el-tabs v-model="currentTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待付款" name="0" />
        <el-tab-pane label="待发货" name="1" />
        <el-tab-pane label="待收货" name="2" />
        <el-tab-pane label="已完成" name="3" />
      </el-tabs>

      <div v-loading="loading">
        <div v-if="orders.length === 0">
          <el-empty description="暂无订单" />
        </div>

        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <span>订单号：{{ order.orderNo }}</span>
            <span class="order-time">{{ order.createTime }}</span>
            <el-tag :color="statusMap[order.status]?.color" style="color:#fff">
              {{ statusMap[order.status]?.label }}
            </el-tag>
          </div>
          <div class="order-body" @click="router.push(`/orders/${order.id}`)">
            <div class="order-info">
              <p>收货人：{{ order.receiverName }}</p>
              <p>合计：<span class="price">¥{{ order.payAmount }}</span></p>
            </div>
          </div>
          <div class="order-actions">
            <el-button v-if="order.status === 0" type="danger" size="small" @click.stop="handlePay(order)">立即付款</el-button>
            <el-button v-if="order.status === 0" size="small" @click.stop="handleCancel(order)">取消订单</el-button>
            <el-button v-if="order.status === 2" type="primary" size="small" @click.stop="handleConfirm(order)">确认收货</el-button>
            <el-button size="small" @click.stop="router.push(`/orders/${order.id}`)">查看详情</el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.order-card {
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
}
.order-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 16px;
  background: #fafafa;
  font-size: 13px;
  color: #666;
}
.order-time {
  flex: 1;
}
.order-body {
  padding: 16px;
  cursor: pointer;
}
.order-body:hover {
  background: #fafafa;
}
.order-info p {
  margin-bottom: 4px;
}
.price {
  color: #ff4400;
  font-weight: bold;
  font-size: 16px;
}
.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid #f0f0f0;
}
</style>
