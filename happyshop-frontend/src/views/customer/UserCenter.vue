<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { updateUserInfo } from '../../api/auth'
import { getAddresses, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '../../api/address'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('info')

// 个人信息
const userForm = ref({ nickname: '', phone: '', email: '' })
const addressList = ref([])
const showAddressDialog = ref(false)
const addressForm = ref({ receiverName: '', receiverPhone: '', province: '', city: '', district: '', detail: '', isDefault: 0 })
const editingAddressId = ref(null)

onMounted(async () => {
  if (userStore.userInfo) {
    userForm.value = {
      nickname: userStore.userInfo.nickname || '',
      phone: userStore.userInfo.phone || '',
      email: userStore.userInfo.email || ''
    }
  }
  loadAddresses()
})

async function loadAddresses() {
  try {
    const res = await getAddresses()
    addressList.value = res.data || []
  } catch (e) { /* ignored */ }
}

async function handleUpdateInfo() {
  try {
    await updateUserInfo(userForm.value)
    await userStore.fetchUserInfo()
    ElMessage.success('更新成功')
  } catch (e) { /* ignored */ }
}

function openAddressDialog(addr) {
  if (addr) {
    editingAddressId.value = addr.id
    addressForm.value = { ...addr }
  } else {
    editingAddressId.value = null
    addressForm.value = { receiverName: '', receiverPhone: '', province: '', city: '', district: '', detail: '', isDefault: 0 }
  }
  showAddressDialog.value = true
}

async function handleSaveAddress() {
  try {
    if (editingAddressId.value) {
      await updateAddress(editingAddressId.value, addressForm.value)
    } else {
      await addAddress(addressForm.value)
    }
    ElMessage.success('保存成功')
    showAddressDialog.value = false
    loadAddresses()
  } catch (e) { /* ignored */ }
}

async function handleDeleteAddress(id) {
  await ElMessageBox.confirm('确定删除此地址？', '提示', { type: 'warning' })
  try {
    await deleteAddress(id)
    ElMessage.success('已删除')
    loadAddresses()
  } catch (e) { /* ignored */ }
}

async function handleSetDefault(id) {
  try {
    await setDefaultAddress(id)
    ElMessage.success('已设为默认')
    loadAddresses()
  } catch (e) { /* ignored */ }
}
</script>

<template>
  <div class="user-center">
    <el-card>
      <template #header><h3>个人中心</h3></template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="个人信息" name="info">
          <el-form :model="userForm" label-width="80px" style="max-width:500px;margin-top:16px">
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickname" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="userForm.phone" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdateInfo">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="收货地址" name="address">
          <el-button type="primary" size="small" @click="openAddressDialog(null)" style="margin-bottom:16px">新增地址</el-button>
          <div v-for="addr in addressList" :key="addr.id" class="address-card">
            <div class="address-info">
              <p><strong>{{ addr.receiverName }}</strong> {{ addr.receiverPhone }}
                <el-tag v-if="addr.isDefault" type="danger" size="small">默认</el-tag>
              </p>
              <p>{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}</p>
            </div>
            <div class="address-actions">
              <el-button text size="small" @click="openAddressDialog(addr)">编辑</el-button>
              <el-button text size="small" @click="handleDeleteAddress(addr.id)">删除</el-button>
              <el-button v-if="!addr.isDefault" text size="small" @click="handleSetDefault(addr.id)">设为默认</el-button>
            </div>
          </div>
          <el-empty v-if="addressList.length === 0" description="暂无收货地址" />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 地址编辑弹窗 -->
    <el-dialog v-model="showAddressDialog" :title="editingAddressId ? '编辑地址' : '新增地址'" width="500px">
      <el-form :model="addressForm" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.receiverName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addressForm.receiverPhone" />
        </el-form-item>
        <el-form-item label="省份">
          <el-input v-model="addressForm.province" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="addressForm.city" />
        </el-form-item>
        <el-form-item label="区县">
          <el-input v-model="addressForm.district" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="addressForm.detail" type="textarea" />
        </el-form-item>
        <el-form-item label="默认地址">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.address-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 12px;
}
.address-info p {
  margin-bottom: 4px;
}
.address-actions {
  display: flex;
  gap: 4px;
}
</style>
