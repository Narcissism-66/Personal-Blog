<script setup>
import ThemeSwitch from "@/components/ThemeSwitch.vue";
import router from "@/router/index.js";
import {message} from "ant-design-vue";
import {userUserStore} from "@/stores/userStore.js";


const [messageApi, contextHolder] = message.useMessage();
const userStore=userUserStore();

const Qualification=()=>{
  if(userStore.user.role==="游客")
  {
    messageApi.warning("当前身份为游客，不能查看空间")
  }
  else {
    router.push('/personal')
  }
}

</script>



<template>
  <contextHolder/>
  <!-- 触发区域 -->
  <div class="fixed left-0 top-0 h-full w-2 z-50 hover:w-72 group">
    <!-- 导航栏 -->
    <nav  class="fixed left-0 top-0 h-full w-72 bg-gradient-to-br from-white/95 via-white/90 to-white/95 dark:from-gray-900/95 dark:via-gray-900/90 dark:to-gray-900/95 backdrop-blur-xl transform -translate-x-full group-hover:translate-x-0 transition-all duration-300 ease-in-out shadow-2xl">
      <div class="flex flex-col h-full">

        <!-- 导航按钮组 -->
        <div class="flex-1 py-6 px-4">
          <div class="space-y-3">
            <!-- 浏览按钮 -->
            <button class="w-full flex items-center gap-4 px-5 py-3.5 rounded-2xl text-gray-700 dark:text-gray-200 hover:bg-gradient-to-r hover:from-blue-500/10 hover:to-purple-500/10 dark:hover:from-blue-400/10 dark:hover:to-purple-400/10 transition-all duration-300 group/btn" @click="router.push('/')">
              <div class="p-2 rounded-xl bg-blue-500/10 dark:bg-blue-400/10 group-hover/btn:bg-blue-500/20 dark:group-hover/btn:bg-blue-400/20 transition-all duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-blue-600 dark:text-blue-400">
                  <path stroke-linecap="round" stroke-linejoin="round" d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25" />
                </svg>
              </div>
              <span class="font-medium">浏览</span>
            </button>

            <!-- 标签按钮 -->
            <button class="w-full flex items-center gap-4 px-5 py-3.5 rounded-2xl text-gray-700 dark:text-gray-200 hover:bg-gradient-to-r hover:from-blue-500/10 hover:to-purple-500/10 dark:hover:from-blue-400/10 dark:hover:to-purple-400/10 transition-all duration-300 group/btn" @click="router.push('/Classification')">
              <div class="p-2 rounded-xl bg-purple-500/10 dark:bg-purple-400/10 group-hover/btn:bg-purple-500/20 dark:group-hover/btn:bg-purple-400/20 transition-all duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="w-5 h-5 text-purple-600 dark:text-purple-400">
                  <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5">
                    <path d="m18.058 8.536l-1-.613C16.055 7.308 15.554 7 15 7s-1.055.308-2.058.923l-1 .613c-.949.582-1.423.873-1.682 1.342c-.26.47-.26 1.035-.26 2.166v5.865c0 1.929 0 2.893.586 3.492S12.114 22 14 22h2c1.886 0 2.828 0 3.414-.6c.586-.598.586-1.562.586-3.49v-5.866c0-1.131 0-1.697-.26-2.166s-.733-.76-1.682-1.342"/>
                    <path d="M14 7.108c-.639-.613-1.02-.935-1.503-1.056c-.56-.141-1.148.017-2.325.333L9 6.699c-1.113.3-1.67.448-2.056.817c-.387.37-.537.894-.836 1.943l-1.554 5.438c-.51 1.788-.766 2.682-.332 3.387c.372.605 1.862 1.35 3.279 1.716"/>
                    <path d="M14.495 10c.841-.56 1.588-1.457 2.052-2.573c.958-2.305.347-4.67-1.363-5.281c-1.711-.612-3.875.76-4.833 3.065A6 6 0 0 0 10 6.364"/>
                  </g>
                </svg>
              </div>
              <span class="font-medium">标签</span>
            </button>

            <!-- 代码编辑器按钮 -->
            <button class="w-full flex items-center gap-4 px-5 py-3.5 rounded-2xl text-gray-700 dark:text-gray-200 hover:bg-gradient-to-r hover:from-blue-500/10 hover:to-purple-500/10 dark:hover:from-blue-400/10 dark:hover:to-purple-400/10 transition-all duration-300 group/btn" @click="router.push('/code-editor')">
              <div class="p-2 rounded-xl bg-green-500/10 dark:bg-green-400/10 group-hover/btn:bg-green-500/20 dark:group-hover/btn:bg-green-400/20 transition-all duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 256 256" class="text-green-600 dark:text-green-400">
                  <path fill="currentColor" d="M69.12 94.15L28.5 128l40.62 33.85a8 8 0 1 1-10.24 12.29l-48-40a8 8 0 0 1 0-12.29l48-40a8 8 0 0 1 10.24 12.3m176 27.7l-48-40a8 8 0 1 0-10.24 12.3L227.5 128l-40.62 33.85a8 8 0 1 0 10.24 12.29l48-40a8 8 0 0 0 0-12.29m-82.39-89.37a8 8 0 0 0-10.25 4.79l-64 176a8 8 0 0 0 4.79 10.26A8.1 8.1 0 0 0 96 224a8 8 0 0 0 7.52-5.27l64-176a8 8 0 0 0-4.79-10.25"/>
                </svg>
              </div>
              <span class="font-medium">代码编辑器</span>
            </button>
          </div>
        </div>

        <!-- 底部用户信息和主题切换 -->
        <div v-if="userStore.user"  class="border-t border-gray-200/50 dark:border-gray-700/50">
          <div class="p-4">
            <div class="flex items-center gap-4 px-5 py-3.5 rounded-2xl hover:bg-gradient-to-r hover:from-blue-500/10 hover:to-purple-500/10 dark:hover:from-blue-400/10 dark:hover:to-purple-400/10 transition-all duration-300 cursor-pointer group/user" @click="Qualification()">
              <div class="relative">
                <img
                    :src="userStore.user.avator"
                    alt="User Avatar"
                    class="w-10 h-10 rounded-xl object-cover ring-2 ring-blue-500/20 dark:ring-blue-400/20 group-hover/user:ring-blue-500/40 dark:group-hover/user:ring-blue-400/40 transition-all duration-300"
                />
                <div class="absolute -bottom-1 -right-1 w-3 h-3 bg-green-500 rounded-full ring-2 ring-white dark:ring-gray-900"></div>
              </div>
              <div class="flex flex-col">
                <span class="text-sm font-medium text-gray-700 dark:text-gray-200">个人空间</span>
                <span class="text-xs text-gray-500 dark:text-gray-400">{{ userStore.user.role }}</span>
              </div>
            </div>
          </div>
          <div class="px-4 pb-6">
            <div class="p-3 rounded-2xl bg-gradient-to-r from-gray-100 to-gray-50 dark:from-gray-800 dark:to-gray-900">
              <ThemeSwitch/>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </div>

  <main>
    <router-view/>
  </main>
</template>


<style scoped>
@media (max-width: 768px) {
  .hidden-on-small-screens {
    display: none;
  }
}

/* 导航栏样式 */
nav {
  border-right: 1px solid rgba(0, 0, 0, 0.1);
}

:deep(.dark) nav {
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

/* 主内容区域样式 */
main {
  min-height: 100vh;
  /* padding: 1rem; */
  background: transparent;
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
  background: rgba(59, 130, 246, 0.1);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.3s ease-out, height 0.3s ease-out;
}

button:hover::after {
  width: 200%;
  height: 200%;
}

/* 主题切换按钮样式 */
:deep(.theme-switch) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 0.75rem;
  border-radius: 1rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

:deep(.theme-switch:hover) {
  transform: rotate(180deg) scale(1.1);
  background: rgba(255, 255, 255, 0.2);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

/* 响应式布局优化 */
@media (max-width: 640px) {
  .container {
    padding: 0.75rem 1rem;
  }
  
  nav {
    padding: 0.5rem 0;
  }
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(to bottom, #3b82f6, #60a5fa);
  border-radius: 4px;
  transition: all 0.3s ease;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(to bottom, #2563eb, #3b82f6);
}

/* 全局动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in {
  animation: fadeIn 0.5s ease forwards;
}
</style>