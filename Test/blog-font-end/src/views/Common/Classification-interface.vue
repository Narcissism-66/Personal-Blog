<script setup>
import {onMounted, reactive, ref} from 'vue';
import {get} from "@/net/index.js";
import router from "@/router/index.js";
const options =reactive({
  Algorithm:[],
  BlogsByTpe:[],
  Project:[],
  Knowledge:[],
})

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
const IsShow=ref(false);
const modalType=ref(null);
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
  <div class="p-6  relative max-h-screen overflow-hidden">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-24">
      <!-- 算法 -->
      <div class="relative bg-white bg-opacity-50 p-4 rounded-lg shadow-md">
        <div class="flex flex-nowrap">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class=" size-6 hover:scale-110  active:scale-90 hc-transition">
            <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/><path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/><circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/></g>
          </svg>
          <h2 class="text-xl font-semibold mb-4 font-kai text-gray-700">算法</h2>
        </div>
        <ul>
          <li v-for="blog in options.Algorithm"  class="mb-2 flex flex-wrap">
            <div @click="blogClickHandle(blog.id)" class="hover:underline cursor-pointer select-none size-6 font-kai w-fit text-green-600">{{blog.title}}</div>
          </li>
        </ul>
        <span @click="getBlogsOrderByType('算法')" class="hover:underline select-none cursor-pointer absolute bottom-4 right-4 w-fit font-kai text-yellow-600">更多</span>
      </div>

      <!-- 项目 -->
      <div class="relative bg-white bg-opacity-50 p-4 rounded-lg shadow-md">
        <div class="flex flex-nowrap">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class=" size-6 hover:scale-110  active:scale-90 hc-transition">
            <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/><path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/><circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/></g>
          </svg>
          <h2   class="text-xl font-semibold mb-4 font-kai text-gray-700">项目</h2>
        </div>
        <ul>
          <li v-for="blog in options.Project"  class="mb-2 flex flex-wrap">
            <div @click="blogClickHandle(blog.id)"  class="hover:underline cursor-pointer select-none size-6 font-kai w-fit text-green-600">{{blog.title}}</div>
          </li>
        </ul>
        <span @click="getBlogsOrderByType('项目')" class="hover:underline select-none cursor-pointer absolute bottom-4 right-4 w-fit font-kai text-yellow-600">更多</span>
      </div>

      <!-- 知识点 -->
      <div class="relative bg-white bg-opacity-50 p-4 rounded-lg shadow-md">
        <div class="flex flex-nowrap">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class=" size-6 hover:scale-110  active:scale-90 hc-transition">
            <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="m15 5l6.3 6.3a2.4 2.4 0 0 1 0 3.4L17 19"/><path d="M9.586 5.586A2 2 0 0 0 8.172 5H3a1 1 0 0 0-1 1v5.172a2 2 0 0 0 .586 1.414L8.29 18.29a2.426 2.426 0 0 0 3.42 0l3.58-3.58a2.426 2.426 0 0 0 0-3.42z"/><circle cx="6.5" cy="9.5" r=".5" fill="currentColor"/></g>
          </svg>
          <h2 class="text-xl font-semibold mb-4 font-kai text-gray-700">小知识</h2>
        </div>
        <ul>
          <li v-for="blog in options.Knowledge"  class="mb-2 flex flex-wrap">
            <div @click="blogClickHandle(blog.id)"  class="hover:underline cursor-pointer select-none size-6 font-kai w-fit text-green-600">{{blog.title}}</div>
          </li>
        </ul>
        <span @click="getBlogsOrderByType('小知识')" class="hover:underline select-none cursor-pointer absolute bottom-4 right-4 w-fit font-kai text-yellow-600">更多</span>
      </div>
    </div>
    <div v-if="IsShow" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div class="bg-white bg-opacity-75  p-6 rounded-xl shadow-lg max-w-lg w-full relative max-h-96 overflow-y-auto scrollbar-hide">
        <!-- Fixed Header Container -->
        <div class="sticky top-0  z-10 ">
          <h2 class="text-2xl font-semibold mb-4 font-kai text-gray-700 select-none cursor-pointer">{{ modalType }}</h2>
          <button @click="CheckIsShow" class="absolute right-2 top-1 text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <!-- Scrollable Content -->
        <ul class="mt-4 ">
          <li v-for="blog in options.Blogs" class="mb-2 flex  flex-wrap">
            <div @click="blogClickHandle(blog.id)" class="hover:underline cursor-pointer select-none size-6 font-kai w-fit text-green-600">{{ blog.title }}</div>
          </li>
        </ul>
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
  -ms-overflow-style: none; /*-ms-overflow-style: none;: 这个属性用于隐藏 IE 和 Edge 浏览器中的滚动条。*/
  scrollbar-width: none;  /*scrollbar-width: none;: 这个属性用于隐藏 Firefox 浏览器中的滚动条。*/
}
</style>




