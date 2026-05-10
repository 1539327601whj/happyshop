import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCartList, addToCart as addToCartApi, updateCartItem, removeCartItem } from '../api/cart'

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref([])
  const totalCount = ref(0)
  const totalPrice = ref(0)

  async function fetchCart() {
    const res = await getCartList()
    cartItems.value = res.data || []
    calcTotal()
  }

  async function addItem(data) {
    await addToCartApi(data)
    await fetchCart()
  }

  async function updateItem(id, data) {
    await updateCartItem(id, data)
    await fetchCart()
  }

  async function removeItem(id) {
    await removeCartItem(id)
    await fetchCart()
  }

  function calcTotal() {
    let count = 0
    let price = 0
    cartItems.value.forEach(item => {
      if (item.checked) {
        count += item.quantity
        price += item.price * item.quantity
      }
    })
    totalCount.value = count
    totalPrice.value = price
  }

  return { cartItems, totalCount, totalPrice, fetchCart, addItem, updateItem, removeItem, calcTotal }
})
