<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { useCartStore } from '../../stores/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const searchKeyword = ref('')

const isLoggedIn = computed(() => userStore.isLoggedIn())
const userInfo = computed(() => userStore.userInfo)

function handleSearch() {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/products', query: { keyword: searchKeyword.value } })
  }
}

function handleLogout() {
  userStore.logout()
  router.push('/login')
}

if (isLoggedIn.value) {
  cartStore.fetchCart()
}
</script>

<template>
  <div class="layout">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="router.push('/')">
          <el-icon size="28" color="#ff4400"><ShoppingCart /></el-icon>
          <span class="logo-text">HappyShop</span>
        </div>

        <div class="search-bar">
          <el-input v-model="searchKeyword" placeholder="搜索商品" @keyup.enter="handleSearch" clearable>
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>

        <div class="header-actions">
          <template v-if="isLoggedIn">
            <el-badge :value="cartStore.totalCount" :hidden="cartStore.totalCount === 0" class="cart-badge">
              <el-button text @click="router.push('/cart')">
                <el-icon size="20"><ShoppingCart /></el-icon>
              </el-button>
            </el-badge>
            <el-dropdown trigger="click">
              <span class="user-info">
                <el-avatar :size="32" :src="userInfo?.avatar">{{ userInfo?.nickname?.[0] || 'U' }}</el-avatar>
                <span class="username">{{ userInfo?.nickname || userInfo?.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/user')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/orders')">我的订单</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/favorites')">我的收藏</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="router.push('/login')">登录</el-button>
            <el-button @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 导航菜单 -->
    <nav class="nav-menu">
      <div class="nav-content">
        <el-menu mode="horizontal" :ellipsis="false" router>
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/products">全部商品</el-menu-item>
          <el-menu-item index="/cart">购物车</el-menu-item>
          <el-menu-item index="/orders">我的订单</el-menu-item>
        </el-menu>
      </div>
    </nav>

    <!-- 主内容 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2026 HappyShop - 在线购物平台</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 30px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  flex-shrink: 0;
}
.logo-text {
  font-size: 22px;
  font-weight: bold;
  color: #ff4400;
}
.search-bar {
  flex: 1;
  max-width: 500px;
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}
.username {
  font-size: 14px;
  color: #333;
}
.cart-badge {
  margin-right: 8px;
}
.nav-menu {
  background: #fff;
  border-bottom: 1px solid #eee;
}
.nav-content {
  max-width: 1200px;
  margin: 0 auto;
}
.main-content {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  width: 100%;
}
.footer {
  background: #333;
  color: #999;
  text-align: center;
  padding: 20px;
  margin-top: auto;
}
.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
