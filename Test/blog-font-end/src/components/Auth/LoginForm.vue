<script setup>
import {reactive, ref} from "vue";
import { message } from 'ant-design-vue';
const [messageApi, contextHolder] = message.useMessage();
import {post} from "@/net/index.js";
import router from "@/router/index.js";
import {useThemeStore} from "@/stores/theme.js";
const themeStore=useThemeStore();
// const props=defineProps(['isLoginComponent'])
const IsLogin=ref(false);
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
const checkLoginHandler=()=>{
  IsLogin.value=!IsLogin.value;
}
</script>

<script>

</script>

<template>
  <contextHolder/>
  <div  class="flex mx-auto items-center justify-center min-w-screen-lg  min-h-screen bg ">
    <svg v-if="IsLogin===false" @click="checkLoginHandler" xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 24 24" class="size-10 text-fuchsia-300">
      <path fill="currentColor" d="M21.5 9h-5l1.86-1.86A7.99 7.99 0 0 0 12 4c-4.42 0-8 3.58-8 8c0 1.83.61 3.5 1.64 4.85c1.22-1.4 3.51-2.35 6.36-2.35s5.15.95 6.36 2.35A7.95 7.95 0 0 0 20 12h2c0 5.5-4.5 10-10 10S2 17.5 2 12S6.5 2 12 2c3.14 0 5.95 1.45 7.78 3.72L21.5 4zM12 20c1.9 0 3.64-.66 5-1.76c-.64-1.01-2.55-1.74-5-1.74s-4.36.73-5 1.74c1.36 1.1 3.1 1.76 5 1.76m0-14c1.93 0 3.5 1.57 3.5 3.5S13.93 13 12 13s-3.5-1.57-3.5-3.5S10.07 6 12 6m0 2c-.83 0-1.5.67-1.5 1.5S11.17 11 12 11s1.5-.67 1.5-1.5S12.83 8 12 8"/>
    </svg>
    <div v-if="IsLogin===true" class="p-8 rounded-lg shadow-md w-96 text-blue-500 bg-opacity-25 bg-white">
      <h2 class="text-3xl font-semibold mb-6 text-center font-kai text-pink-400">登录</h2>
      <div class="space-y-6">
        <div class="flex flex-col">
          <label for="username" class="text-xl font-medium text-pink-400 font-kai mb-2">用户</label>
          <input
              type="text"
              id="username"
              v-model="loginform.username"
              class="text-fuchsia-300 mt-1 block w-full px-3 py-2 border-none rounded-md shadow-sm focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm bg-transparent placeholder-pink-400"              placeholder="请输入用户名"
          />
        </div>
        <div class="flex flex-col">
          <label for="password" class="text-xl font-medium text-pink-400 font-kai mb-2">密码</label>
          <input
              type="password"
              id="password"
              v-model="loginform.password"
              class="text-fuchsia-300 mt-1 block w-full px-3 py-2 border-none rounded-md shadow-sm focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm bg-transparent placeholder-pink-400"              placeholder="请输入密码"
          />
        </div>
        <button
            @click="loginHandler()"
            type="submit"
            class="bg-opacity-0 font-bold w-full text-2xl text-pink-400 py-2 px-4 rounded-md font-kai bg-pink-500 hover:text-pink-700 transition-colors duration-300"
        >
          登录
        </button>
        <span class="text-pink-400  ml-14 font-kai">游客账号：222 密码：222</span>
      </div>
    </div>
  </div>
</template>


<style scoped>
/* 你可以在这里添加一些自定义样式 */
</style>