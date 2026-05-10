import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // 用户端
  {
    path: '/',
    component: () => import('../views/customer/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/customer/Home.vue') },
      { path: 'products', name: 'Products', component: () => import('../views/customer/Products.vue') },
      { path: 'products/:id', name: 'ProductDetail', component: () => import('../views/customer/ProductDetail.vue') },
      { path: 'cart', name: 'Cart', component: () => import('../views/customer/Cart.vue'), meta: { requireAuth: true } },
      { path: 'checkout', name: 'Checkout', component: () => import('../views/customer/Checkout.vue'), meta: { requireAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('../views/customer/Orders.vue'), meta: { requireAuth: true } },
      { path: 'orders/:id', name: 'OrderDetail', component: () => import('../views/customer/OrderDetail.vue'), meta: { requireAuth: true } },
      { path: 'user', name: 'UserCenter', component: () => import('../views/customer/UserCenter.vue'), meta: { requireAuth: true } },
      { path: 'favorites', name: 'Favorites', component: () => import('../views/customer/Favorites.vue'), meta: { requireAuth: true } },
    ]
  },
  { path: '/login', name: 'Login', component: () => import('../views/customer/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/customer/Register.vue') },

  // 商家端
  {
    path: '/merchant',
    component: () => import('../views/merchant/MerchantLayout.vue'),
    meta: { requireAuth: true, role: 'MERCHANT' },
    children: [
      { path: '', name: 'MerchantDashboard', component: () => import('../views/merchant/Dashboard.vue') },
      { path: 'products', name: 'MerchantProducts', component: () => import('../views/merchant/Products.vue') },
      { path: 'orders', name: 'MerchantOrders', component: () => import('../views/merchant/Orders.vue') },
    ]
  },

  // 管理员端
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requireAuth: true, role: 'ADMIN' },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/admin/Users.vue') },
      { path: 'merchants', name: 'AdminMerchants', component: () => import('../views/admin/Merchants.vue') },
      { path: 'products', name: 'AdminProducts', component: () => import('../views/admin/Products.vue') },
      { path: 'categories', name: 'AdminCategories', component: () => import('../views/admin/Categories.vue') },
      { path: 'promotions', name: 'AdminPromotions', component: () => import('../views/admin/Promotions.vue') },
    ]
  },

  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('../views/customer/NotFound.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || 'null')

  if (to.meta.requireAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }

  if (to.meta.role && user?.role !== to.meta.role) {
    next('/')
    return
  }

  next()
})

export default router
