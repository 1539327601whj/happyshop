<script setup>
import { ref, onMounted } from 'vue'
import { getOrders } from '../../api/order'
import { ElMessage } from 'element-plus'

const orders = ref([])
const total = ref(0)
const loading = ref(false)

const statusMap = {
  0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消', 5: '退款中'
}

onMounted(() => loadOrders())

async function loadOrders() {
  loading.value = true
  try {
    const res = await getOrders({ current: 1, size: 20 })
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) { /* ignored */ }
  finally { loading.value = false }
}

async function handleShip(order) {
  // TODO: 调用发货API
  ElMessage.success('已发货')
  loadOrders()
}
</script>

<template>
  <div>
    <h3 style="margin-bottom:16px">订单管理</h3>
    <el-table :data="orders" v-loading="loading" style="width:100%">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="receiverName" label="收货人" width="120" />
      <el-table-column prop="payAmount" label="金额" width="120">
        <template #default="{ row }"><span style="color:#ff4400">¥{{ row.payAmount }}</span></template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 3 ? 'success' : row.status === 4 ? 'info' : 'warning'">
            {{ statusMap[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" type="primary" text size="small" @click="handleShip(row)">发货</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
