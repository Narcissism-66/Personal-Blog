<script setup>
import {onMounted, reactive, ref, watch} from 'vue';
import {get} from "@/net/index.js";
import router from "@/router/index.js";

const options = reactive({
  Algorithm:[],
  BlogsByTpe:[],
  Project:[],
  Knowledge:[],
})

const IsShow = ref(false);
const modalType = ref(null);

// 监听弹窗状态变化
watch(IsShow, (newValue) => {
  if (newValue) {
    // 弹窗打开时禁用背景滚动
    document.body.style.overflow = 'hidden';
  } else {
    // 弹窗关闭时恢复背景滚动
    document.body.style.overflow = '';
  }
});

const getBlogsOrderByType=(type)=>{
  get('api/blog/getBlogsOrderByType',{
    type:type,
  },(message,data)=>{
    options.Blogs=data;
    modalType.value=type;
    CheckIsShow();
  })
}

const getBlogsOfKnowledge=()=>{
  get('api/blog/Knowledge',{},(message,data)=>{
    options.Knowledge=data;
  })
}

const getBlogsOfAlgorithm=()=>{
  get('api/blog/Algorithm',{},(message,data)=>{
    options.Algorithm=data;
  })
}

const getBlogsOfProject=()=>{
  get('api/blog/Project',{},(message,data)=>{
    options.Project=data;
  })
}

const CheckIsShow=()=>{
  IsShow.value=!IsShow.value;
}

const blogClickHandle=(id)=>{
  router.push("/view/"+id)
}

onMounted(()=>{
  getBlogsOfKnowledge();
  getBlogsOfAlgorithm();
  getBlogsOfProject();
})
</script>


<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-purple-50 dark:from-purple-900/20 dark:to-purple-800/20 p-6 relative">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mt-24">
      <!-- 算法 -->
      <div class="group relative bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm p-6 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-500 hover:-translate-y-2 h-[400px] flex flex-col">
        <div class="absolute inset-0 bg-gradient-to-br from-blue-500/10 to-purple-500/10 dark:from-blue-400/20 dark:to-purple-400/20 rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
        <div class="relative flex-1">
          <div class="flex items-center space-x-3 mb-6">
            <div class="p-2 bg-blue-100 rounded-xl group-hover:bg-blue-200 transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="size-6 text-blue-600 group-hover:scale-110 transition-transform duration-300">
                <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                  <path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/>
                  <path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/>
                  <circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/>
                </g>
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-gray-800 group-hover:text-blue-600 transition-colors duration-300">算法</h2>
          </div>
          <ul class="space-y-3">
            <li v-for="(blog, index) in options.Algorithm" :key="blog.id" 
                class="transform transition-all duration-300 hover:translate-x-2"
                :style="{ animationDelay: `${index * 0.1}s` }">
              <div @click="blogClickHandle(blog.id)" 
                   class="flex items-center space-x-2 p-2 rounded-lg hover:bg-blue-50 cursor-pointer group/item">
                <span class="w-1.5 h-1.5 bg-blue-400 rounded-full group-hover/item:scale-150 transition-transform duration-300"></span>
                <span class="text-gray-600 group-hover/item:text-blue-600 transition-colors duration-300  dark:text-gray-200">{{blog.title}}</span>
              </div>
            </li>
          </ul>
        </div>
        <div class="mt-auto pt-4">
          <button @click="getBlogsOrderByType('算法')" 
                  class="group relative w-full px-4 py-2 overflow-hidden rounded-lg bg-gradient-to-r from-blue-500 to-blue-600 text-white font-medium transition-all duration-300 hover:from-blue-600 hover:to-blue-700 hover:shadow-lg">
            <span class="relative z-10 flex items-center justify-center">
              查看更多
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-2 transform group-hover:translate-x-1 transition-transform duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </span>
            <div class="absolute inset-0 bg-white opacity-0 group-hover:opacity-10 transition-opacity duration-300"></div>
          </button>
        </div>
      </div>

      <!-- 项目 -->
      <div class="group relative bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm p-6 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-500 hover:-translate-y-2 h-[400px] flex flex-col">
        <div class="absolute inset-0 bg-gradient-to-br from-purple-500/10 to-pink-500/10 dark:from-purple-400/20 dark:to-pink-400/20 rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
        <div class="relative flex-1">
          <div class="flex items-center space-x-3 mb-6">
            <div class="p-2 bg-purple-100 rounded-xl group-hover:bg-purple-200 transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="size-6 text-purple-600 group-hover:scale-110 transition-transform duration-300">
                <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                  <path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/>
                  <path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/>
                  <circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/>
                </g>
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-gray-800 group-hover:text-purple-600 transition-colors duration-300">项目</h2>
          </div>
          <ul class="space-y-3">
            <li v-for="(blog, index) in options.Project" :key="blog.id" 
                class="transform transition-all duration-300 hover:translate-x-2"
                :style="{ animationDelay: `${index * 0.1}s` }">
              <div @click="blogClickHandle(blog.id)" 
                   class="flex items-center space-x-2 p-2 rounded-lg hover:bg-purple-50 cursor-pointer group/item">
                <span class="w-1.5 h-1.5 bg-purple-400 rounded-full group-hover/item:scale-150 transition-transform duration-300"></span>
                <span class="text-gray-600 group-hover/item:text-purple-600 transition-colors duration-300 dark:text-gray-200">{{blog.title}}</span>
              </div>
            </li>
          </ul>
        </div>
        <div class="mt-auto pt-4">
          <button @click="getBlogsOrderByType('项目')" 
                  class="group relative w-full px-4 py-2 overflow-hidden rounded-lg bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium transition-all duration-300 hover:from-purple-600 hover:to-purple-700 hover:shadow-lg">
            <span class="relative z-10 flex items-center justify-center">
              查看更多
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-2 transform group-hover:translate-x-1 transition-transform duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </span>
            <div class="absolute inset-0 bg-white opacity-0 group-hover:opacity-10 transition-opacity duration-300"></div>
          </button>
        </div>
      </div>

      <!-- 知识点 -->
      <div class="group relative bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm p-6 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-500 hover:-translate-y-2 h-[400px] flex flex-col">
        <div class="absolute inset-0 bg-gradient-to-br from-pink-500/10 to-red-500/10 dark:from-pink-400/20 dark:to-red-400/20 rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
        <div class="relative flex-1">
          <div class="flex items-center space-x-3 mb-6">
            <div class="p-2 bg-pink-100 rounded-xl group-hover:bg-pink-200 transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="size-6 text-pink-600 group-hover:scale-110 transition-transform duration-300">
                <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                  <path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/>
                  <path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/>
                  <circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/>
                </g>
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-gray-800 group-hover:text-pink-600 transition-colors duration-300">小知识</h2>
          </div>
          <ul class="space-y-3">
            <li v-for="(blog, index) in options.Knowledge" :key="blog.id" 
                class="transform transition-all duration-300 hover:translate-x-2"
                :style="{ animationDelay: `${index * 0.1}s` }">
              <div @click="blogClickHandle(blog.id)" 
                   class="flex items-center space-x-2 p-2 rounded-lg hover:bg-pink-50 cursor-pointer group/item">
                <span class="w-1.5 h-1.5 bg-pink-400 rounded-full group-hover/item:scale-150 transition-transform duration-300"></span>
                <span class="text-gray-600 group-hover/item:text-pink-600 transition-colors duration-300  dark:text-gray-200">{{blog.title}}</span>
              </div>
            </li>
          </ul>
        </div>
        <div class="mt-auto pt-4">
          <button @click="getBlogsOrderByType('小知识')" 
                  class="group relative w-full px-4 py-2 overflow-hidden rounded-lg bg-gradient-to-r from-pink-500 to-pink-600 text-white font-medium transition-all duration-300 hover:from-pink-600 hover:to-pink-700 hover:shadow-lg">
            <span class="relative z-10 flex items-center justify-center">
              查看更多
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-2 transform group-hover:translate-x-1 transition-transform duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </span>
            <div class="absolute inset-0 bg-white opacity-0 group-hover:opacity-10 transition-opacity duration-300"></div>
          </button>
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
    <div v-if="IsShow" 
         class="fixed inset-0 flex items-center justify-center bg-black/30 dark:bg-black/50 backdrop-blur-sm z-50 transition-opacity duration-300">
      <div class="bg-white/95 dark:bg-gray-800/95 backdrop-blur-md rounded-2xl shadow-2xl max-w-3xl w-full relative max-h-[85vh] overflow-hidden">
        <!-- 头部 -->
        <div class="bg-white/95 backdrop-blur-md p-8 border-b border-gray-100  dark:bg-gray-800">
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-3">
              <div class="p-2 rounded-xl dark:bg-gray-800" :class="{
                'bg-blue-100': modalType === '算法',
                'bg-purple-100': modalType === '项目',
                'bg-pink-100': modalType === '小知识'
              }">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="size-6" :class="{
                  'text-blue-600': modalType === '算法',
                  'text-purple-600': modalType === '项目',
                  'text-pink-600': modalType === '小知识'
                }">
                  <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                    <path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/>
                    <path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/>
                    <circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/>
                  </g>
                </svg>
              </div>
              <h2 class="text-3xl font-bold" :class="{
                'text-blue-600': modalType === '算法',
                'text-purple-600': modalType === '项目',
                'text-pink-600': modalType === '小知识'
              }">{{ modalType }}</h2>
            </div>
            <button @click="CheckIsShow" 
                    class="p-2 hover:bg-gray-100 rounded-full transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <!-- 内容区域 -->
        <div class="overflow-y-auto max-h-[calc(85vh-120px)] p-8 pt-6 scrollbar-hide bg-white dark:bg-gray-800">
          <ul class="space-y-2">
            <li v-for="(blog, index) in options.Blogs" :key="blog.id" 
                class="transform transition-all duration-300 hover:translate-x-2"
                :style="{ animationDelay: `${index * 0.1}s` }">
              <div @click="blogClickHandle(blog.id)" 
                   class="group flex items-center p-4 rounded-xl dark:hover:bg-gray-500 hover:bg-gray-50 cursor-pointer transition-colors duration-300">
                <div class="flex-1 flex items-center space-x-4">
                  <span class="w-2 h-2 rounded-full" :class="{
                    'bg-blue-400': modalType === '算法',
                    'bg-purple-400': modalType === '项目',
                    'bg-pink-400': modalType === '小知识'
                  }"></span>
                  <span class="text-gray-600 group-hover:text-gray-900 transition-colors duration-300  dark:text-gray-200">{{ blog.title }}</span>
                </div>
                <div class="flex items-center space-x-2">
                  <span class="text-sm text-gray-400 group-hover:text-gray-600 transition-colors duration-300">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                  </span>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>



<style scoped>
/*用于隐藏滚动条,::-webkit-scrollbar:这个伪元素用于隐藏 WebKit 浏览器（如 Chrome、Safari）中的滚动条。*/
.scrollbar-hide::-webkit-scrollbar {
  display: none;
}

.scrollbar-hide {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

/* 列表项动画 */
li {
  animation: slideIn 0.5s ease-out forwards;
  opacity: 0;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 背景动画 */
.bg-gradient-to-br {
  background-size: 200% 200%;
  animation: gradientBG 15s ease infinite;
}

@keyframes gradientBG {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style>




