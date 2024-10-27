<script setup>
import {reactive} from "vue";
import { message } from 'ant-design-vue';
const [messageApi, contextHolder] = message.useMessage();
import {post} from "@/net/index.js";
import router from "@/router/index.js";
import {useThemeStore} from "@/stores/theme.js";
const themeStore=useThemeStore();
// const props=defineProps(['isLoginComponent'])
const emits=defineEmits(['update:test']);
const goAnotherForm=()=>{
  router.push("/register");
}
const goHome=()=>{
  emits('update:test',3)
}
const loginform=reactive([
  {
    username:"",
    password:''
  }
])

const loginHandler=()=>{
  post('api/auth/login',{
    username:loginform.username,
    password:loginform.password
  },(message,data) => {
    messageApi.success(message)
    localStorage.setItem("authToken",data);
    setTimeout(function() {
      router.push('/')
    }, 1000)
  }, (message, data) => {
    messageApi.warning(message)
  }, (message, data) => {
    messageApi.error("发生错误，请联系管理员~")
  })
}

</script>

<script>

</script>

<template>
  <contextHolder/>
  <div class="themeStore.currentTheme==='light' min-h-screen flex items-center justify-center bg-gray-100 min-w-screen ">
    <div class="bg-blue-100 p-8 rounded-lg shadow-md w-96 text-blue-500">
      <h2 class="text-2xl font-semibold mb-6 text-center">登录</h2>
      <div>
        <div class="mb-4">
          <label for="username" class="block text-sm font-medium text-gray-700">用户名</label>
          <input
              type="text"
              id="username"
              v-model="loginform.username"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="请输入用户名"
          />
        </div>
        <div class="mb-6">
          <label for="password" class="block text-sm font-medium text-gray-700">密码</label>
          <input
              type="password"
              id="password"
              v-model="loginform.password"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="请输入密码"
          />
        </div>
        <button
            @click="loginHandler()"
            type="submit"
            class=" w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          登录
        </button>
<!--        <div class="text-center">-->
<!--          <a class="select-none">还没有账号？</a>-->
<!--          <a @click="goAnotherForm" class="text-neutral-500 hover:text-blue-700 hover:underline cursor-pointer">注册一个</a>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>


<style scoped>
/* 你可以在这里添加一些自定义样式 */
</style>