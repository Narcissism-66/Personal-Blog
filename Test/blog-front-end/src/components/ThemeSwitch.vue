<script setup>
import {useThemeStore} from "@/stores/theme.js";
import router from "@/router/index.js";
import {userUserStore} from "@/stores/userStore.js";
import {message} from "ant-design-vue";
import {ref} from "vue";
import {post} from "@/net/index.js";
const [messageApi, contextHolder] = message.useMessage();
const themeStore=useThemeStore();
const userStore=userUserStore();

const role=ref(userStore.user.role);
const toggleThemeHandler=()=>{
  themeStore.toggleTheme();
}
const Qualification_blog=()=>{
  if(userStore.user.role==="游客")
  {
    messageApi.warning("当前身份为游客，不能发布文章")
  }
  else {
    router.push('/edit')
  }
}
const Qualification_notification=()=>{
  if(userStore.user.role==="游客")
  {
    messageApi.warning("当前身份为游客，不能查看消息")
  }
  else {
    router.push('/notification')
  }
}

//更新为离线
const UpdateOnline = () => {
  post('api/user/UpdateOnline', {
    userId: userStore.user.id,
    isOnline: "false"
  }, (message) => {
    messageApi.success("离线啦~")
  });
};
const logout=()=>{
  UpdateOnline();
  userStore.logout();
  router.push('/auth')
}
</script>

<template>
  <contextHolder/>
  <div class="flex flex-col gap-3">
    <!-- 发布按钮 -->
    <button class="w-full flex items-center gap-3 px-4 py-3 rounded-xl bg-gradient-to-r from-blue-500/20 to-purple-500/20 hover:from-blue-500/30 hover:to-purple-500/30 active:from-blue-500/40 active:to-purple-500/40 transition-all duration-300 group" @click="Qualification_blog">
      <div class="p-1.5 rounded-lg bg-white/10 group-hover:bg-white/20 transition-all duration-300">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-blue-600 dark:text-blue-400">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
        </svg>
      </div>
      <span class="font-medium text-blue-600 dark:text-blue-400">发布文章</span>
    </button>

    <!-- 计划按钮 -->
    <button class="w-full flex items-center gap-3 px-4 py-3 rounded-xl bg-gradient-to-r from-purple-500/20 to-pink-500/20 hover:from-purple-500/30 hover:to-pink-500/30 active:from-purple-500/40 active:to-pink-500/40 transition-all duration-300 group" @click="router.push('/plan')">
      <div class="p-1.5 rounded-lg bg-white/10 group-hover:bg-white/20 transition-all duration-300">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 26 26" class="w-5 h-5 text-purple-600 dark:text-purple-400">
          <path fill="currentColor" d="M7.875 0a1 1 0 0 0-.656.375L3.812 4.656l-2.25-1.5A1.014 1.014 0 1 0 .438 4.844l3 2a1 1 0 0 0 1.344-.219l4-5A1 1 0 0 0 7.875 0M12 3v2h14V3zM7.875 9a1 1 0 0 0-.656.375l-3.407 4.281l-2.25-1.5a1.014 1.014 0 1 0-1.125 1.688l3 2a1 1 0 0 0 1.344-.219l4-5A1 1 0 0 0 7.875 9M12 12v2h14v-2zm-4.125 6a1 1 0 0 0-.656.375l-3.407 4.281l-2.25-1.5a1.014 1.014 0 1 0-1.125 1.688l3 2a1 1 0 0 0 1.344-.219l4-5A1 1 0 0 0 7.875 18M12 21v2h14v-2z"/>
        </svg>
      </div>
      <span class="font-medium text-purple-600 dark:text-purple-400">计划</span>
    </button>

    <!-- 管理按钮 -->
    <button v-if="role==='管理员'||role==='创始人'" class="w-full flex items-center gap-3 px-4 py-3 rounded-xl bg-gradient-to-r from-indigo-500/20 to-blue-500/20 hover:from-indigo-500/30 hover:to-blue-500/30 active:from-indigo-500/40 active:to-blue-500/40 transition-all duration-300 group" @click="router.push('/admin')">
      <div class="p-1.5 rounded-lg bg-white/10 group-hover:bg-white/20 transition-all duration-300">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="w-5 h-5 text-indigo-600 dark:text-indigo-400">
          <g fill="none" stroke="currentColor" stroke-width="1.5">
            <circle cx="12" cy="9" r="2"/>
            <path d="M16 15c0 1.105 0 2-4 2s-4-.895-4-2s1.79-2 4-2s4 .895 4 2Z"/>
            <path stroke-linecap="round" d="M3 10.417c0-3.198 0-4.797.378-5.335c.377-.537 1.88-1.052 4.887-2.081l.573-.196C10.405 2.268 11.188 2 12 2s1.595.268 3.162.805l.573.196c3.007 1.029 4.51 1.544 4.887 2.081C21 5.62 21 7.22 21 10.417v1.574c0 2.505-.837 4.437-2 5.913M3.193 14c.857 4.298 4.383 6.513 6.706 7.527c.721.315 1.082.473 2.101.473c1.02 0 1.38-.158 2.101-.473c.579-.252 1.231-.58 1.899-.994"/>
          </g>
        </svg>
      </div>
      <span class="font-medium text-indigo-600 dark:text-indigo-400">管理控制台</span>
    </button>

    <!-- 主题切换按钮 -->
    <button @click="toggleThemeHandler" class="w-full flex items-center gap-3 px-4 py-3 rounded-xl bg-gradient-to-r from-purple-600/95 via-purple-500/90 to-purple-600/95 dark:from-purple-700/95 dark:via-purple-900/90 dark:to-purple-700/95 hover:from-purple-600/90 hover:via-purple-500/85 hover:to-purple-600/90 dark:hover:from-purple-700/90 dark:hover:via-purple-600/85 dark:hover:to-purple-700/90 transition-all duration-300 group">
      <div class="p-1.5 rounded-lg bg-purple-300/10 dark:bg-purple-200/10 group-hover:bg-purple-300/20 dark:group-hover:bg-purple-200/20 transition-all duration-300">
        <svg v-if="themeStore.currentTheme === 'light'" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="w-5 h-5 text-purple-300 dark:text-purple-200">
          <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
            <path stroke-dasharray="36" stroke-dashoffset="36" d="M12 7c2.76 0 5 2.24 5 5c0 2.76 -2.24 5 -5 5c-2.76 0 -5 -2.24 -5 -5c0 -2.76 2.24 -5 5 -5">
              <animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="36;0"/>
            </path>
            <g>
              <path stroke-dasharray="2" stroke-dashoffset="2" d="M12 19v1M19 12h1M12 5v-1M5 12h-1">
                <animate fill="freeze" attributeName="d" begin="0.5s" dur="0.2s" values="M12 19v1M19 12h1M12 5v-1M5 12h-1;M12 21v1M21 12h1M12 3v-1M3 12h-1"/>
                <animate fill="freeze" attributeName="stroke-dashoffset" begin="0.5s" dur="0.2s" values="2;0"/>
              </path>
              <path stroke-dasharray="2" stroke-dashoffset="2" d="M17 17l0.5 0.5M17 7l0.5 -0.5M7 7l-0.5 -0.5M7 17l-0.5 0.5">
                <animate fill="freeze" attributeName="d" begin="0.7s" dur="0.2s" values="M17 17l0.5 0.5M17 7l0.5 -0.5M7 7l-0.5 -0.5M7 17l-0.5 0.5;M18.5 18.5l0.5 0.5M18.5 5.5l0.5 -0.5M5.5 5.5l-0.5 -0.5M5.5 18.5l-0.5 0.5"/>
                <animate fill="freeze" attributeName="stroke-dashoffset" begin="0.7s" dur="0.2s" values="2;0"/>
              </path>
              <animateTransform attributeName="transform" dur="30s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/>
            </g>
          </g>
        </svg>
        <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="w-5 h-5 text-purple-300 dark:text-purple-200">
          <path fill-opacity="0" d="M15.22 6.03L17.75 4.09L14.56 4L13.5 1L12.44 4L9.25 4.09L11.78 6.03L10.87 9.09L13.5 7.28L16.13 9.09L15.22 6.03Z" fill="currentColor">
            <animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.4s" values="0;1"/>
          </path>
          <path fill-opacity="0" d="M19.61 12.25L21.25 11L19.19 10.95L18.5 9L17.81 10.95L15.75 11L17.39 12.25L16.8 14.23L18.5 13.06L20.2 14.23L19.61 12.25Z" fill="currentColor">
            <animate fill="freeze" attributeName="fill-opacity" begin="1s" dur="0.4s" values="0;1"/>
          </path>
          <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
            <g>
              <path stroke-dasharray="2" stroke-dashoffset="4" d="M12 21v1M21 12h1M12 3v-1M3 12h-1">
                <animate fill="freeze" attributeName="stroke-dashoffset" dur="0.2s" values="4;2"/>
              </path>
              <path stroke-dasharray="2" stroke-dashoffset="4" d="M18.5 18.5l0.5 0.5M18.5 5.5l0.5 -0.5M5.5 5.5l-0.5 -0.5M5.5 18.5l-0.5 0.5">
                <animate fill="freeze" attributeName="stroke-dashoffset" begin="0.2s" dur="0.2s" values="4;2"/>
              </path>
              <set fill="freeze" attributeName="opacity" begin="0.5s" to="0"/>
            </g>
            <path d="M7 6 C7 12.08 11.92 17 18 17 C18.53 17 19.05 16.96 19.56 16.89 C17.95 19.36 15.17 21 12 21 C7.03 21 3 16.97 3 12 C3 8.83 4.64 6.05 7.11 4.44 C7.04 4.95 7 5.47 7 6 Z" opacity="0">
              <set fill="freeze" attributeName="opacity" begin="0.5s" to="1"/>
            </path>
          </g>
        </svg>
      </div>
      <span class="font-medium text-purple-300 dark:text-purple-200">{{ themeStore.currentTheme === 'light' ? '深色模式' : '浅色模式' }}</span>
    </button>

    <!-- 退出按钮 -->
    <button class="w-full flex items-center gap-3 px-4 py-3 rounded-xl bg-gradient-to-r from-red-500/20 to-pink-500/20 hover:from-red-500/30 hover:to-pink-500/30 active:from-red-500/40 active:to-pink-500/40 transition-all duration-300 group" @click="logout">
      <div class="p-1.5 rounded-lg bg-white/10 group-hover:bg-white/20 transition-all duration-300">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-red-600 dark:text-red-400">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0 0 13.5 3h-6a2.25 2.25 0 0 0-2.25 2.25v13.5A2.25 2.25 0 0 0 7.5 21h6a2.25 2.25 0 0 0 2.25-2.25V15m3 0 3-3m0 0-3-3m3 3H9" />
        </svg>
      </div>
      <span class="font-medium text-red-600 dark:text-red-400">退出登录</span>
    </button>
  </div>
</template>

<style scoped>
@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: translateX(-2px);
  }
  20%, 40%, 60%, 80% {
    transform: translateX(2px);
  }
}

.animate-shake {
  animation: shake 0.8s ease-in-out infinite;
}

/* 按钮悬停效果 */
button {
  position: relative;
  overflow: hidden;
}

button::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.3s ease-out, height 0.3s ease-out;
}

button:hover::after {
  width: 200%;
  height: 200%;
}
</style>
