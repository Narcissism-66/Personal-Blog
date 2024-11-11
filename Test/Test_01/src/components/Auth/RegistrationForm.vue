<script setup>
import { reactive } from "vue";
import { ElMessage } from 'element-plus';
import { useThemeStore } from "@/stores/theme.js";
import router from "@/router/index.js";
import { post } from "@/net/index.js";
import { message } from "ant-design-vue";
const [messageApi, contextHolder] = message.useMessage();

const emits = defineEmits(['update:isLoginComponent']);
const goAnotherForm = () => {
  router.push("/auth");
}

const themeStore = useThemeStore();
const registerform = reactive({
  username: "",
  password: '',
  mail: '',
  avator: ""
});

const registerHandler = () => {
  post("/api/user/register", {
    username: registerform.username,
    password: registerform.password,
    mail: registerform.mail,
    avator: registerform.avator,
  }, (message) => {
    messageApi.success(message);
  }, (message) => {
    messageApi.warning(message);
  }, (message) => {
    messageApi.error(message);
  })
}

// 处理头像选择
const handleAvatarSelect = (avatarUrl) => {
  registerform.avator = avatarUrl;
}
</script>

<template>
  <contextHolder />
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-blue-100 p-8 rounded-lg shadow-md w-96 text-blue-500">
      <h2 class="text-2xl font-semibold mb-6 text-center">注册</h2>

      <!-- 用户名 -->
      <div class="mb-4">
        <label for="username" class="block text-sm font-medium text-gray-700">用户名</label>
        <input
            type="text"
            v-model="registerform.username"
            class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="请输入用户名"
            required
        />
      </div>

      <!-- 密码 -->
      <div class="mb-4">
        <label for="password" class="block text-sm font-medium text-gray-700">密码</label>
        <input
            type="password"
            v-model="registerform.password"
            class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="请输入密码"
            required
        />
      </div>

      <!-- 邮箱 -->
      <div class="mb-4">
        <label for="email" class="block text-sm font-medium text-gray-700">邮箱</label>
        <input
            type="email"
            id="email"
            v-model="registerform.mail"
            class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="请输入邮箱"
            required
        />
      </div>

      <!-- 头像选择 -->
<!--      为每个头像添加了一个动态类 :class="{ 'border-4 border-blue-500': registerform.avator === avatar.url }";-->
<!--      当头像被选中时，会添加一个蓝色的边框-->
      <div class="mb-6">
        <label for="avatar" class="block text-sm font-medium text-gray-700">选择头像</label>
        <div class="grid grid-cols-4 gap-4 mt-2">
          <div
              v-for="(avatar, index) in avatars"
              :key="index"
              @click="handleAvatarSelect(avatar.url)"
              :class="{ 'border-4 border-green-400 ': registerform.avator === avatar.url }"
              class="cursor-pointer rounded-md hover:scale-105 active:scale-100"
          >
            <img :src="avatar.url" :alt="avatar.alt" class="w-full h-auto rounded-md " />
          </div>
        </div>
      </div>

      <!-- 提交按钮 -->
      <button
          @click="registerHandler"
          class="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        注册
      </button>
      <div class="mt-4 text-center">
        <a class="select-none">已有账号</a>  <a @click="goAnotherForm" class="text-neutral-500 hover:text-blue-700 hover:underline cursor-pointer">马上登陆</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      avatars: [
        { url: "https://narcissism-66.github.io/image/good.jpg", alt: "1" },
        { url: "https://narcissism-66.github.io/image/11.jpg", alt: "2" },
        { url: "https://narcissism-66.github.io/image/99.jpg", alt: "3" },
        { url: "https://narcissism-66.github.io/image/22.jpg", alt: "4" }
      ]
    };
  }
};
</script>

<style scoped>
/* 你可以在这里添加一些自定义样式 */
</style>