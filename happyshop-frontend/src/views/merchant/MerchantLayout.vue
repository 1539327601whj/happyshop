<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'

const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="merchant-layout">
    <el-container style="min-height:100vh">
      <el-aside width="220px" style="background:#001529">
        <div class="logo">
          <h2 style="color:#fff;font-size:18px">商家后台</h2>
        </div>
        <el-menu background-color="#001529" text-color="#ffffffa6" active-text-color="#fff" router>
          <el-menu-item index="/merchant">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/merchant/products">
            <el-icon><Goods /></el-icon>
            <span>商品管理</span>
          </el-menu-item>
          <el-menu-item index="/merchant/orders">
            <el-icon><List /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="display:flex;align-items:center;justify-content:flex-end;background:#fff;box-shadow:0 1px 4px rgba(0,0,0,0.08)">
          <el-dropdown trigger="click">
            <span style="cursor:pointer">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/')">回到前台</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-header>
        <el-main style="background:#f0f2f5;padding:20px">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
