<script setup>
import mn from '@/assets/image/hh.jpg'
import ThemeSwitch from "@/components/ThemeSwitch.vue";
import {useThemeStore} from "@/stores/theme.js";
import router from "@/router/index.js";
import {useRoute} from "vue-router";
import {get} from "@/net/index.js";
import {onMounted, reactive} from "vue";
const themeStore=useThemeStore();
const route=useRoute();
const pid=route.params.id;
const options=reactive({
  data:{
    user:{
      username:"",
      password:"",
      mail:"",
      avator:""
    }
  }
})

const fetchUser=()=>{
  get('api/user/TheUser',
      {id:pid},
      (message,data)=>{
        options.data=data;
      })
}
onMounted(()=>{
  fetchUser();
})
</script>



<template>
  <nav class="basic-color fixed top-0 left-0 w-full shadow-md z-50 shadow-gray-200 dark:shadow-blue-400">
    <div class="container mx-auto flex justify-between items-center py-4 px-6 ">
      <!-- 左侧：网站logo -->

      <div class="flex font-normal gap-4">
        <!--      浏览-->
        <button class="hover:scale-110  active:scale-90" @click="router.push('/')">
          <svg v-if="themeStore.currentTheme==='light'" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-black hover:text-blue-500" >
            <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z" />
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 hover:text-blue-500" >
            <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z" />
          </svg>
        </button>

        <!--      代码-->
        <button class="flex items-center hover:scale-110  active:scale-90">
          <svg  v-if="themeStore.currentTheme==='dark'" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 hover:text-blue-500">
            <path stroke-linecap="round" stroke-linejoin="round" d="M17.25 6.75 22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m7.5-3-4.5 16.5" />
          </svg>
          <svg  v-if="themeStore.currentTheme==='light'" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-black hover:text-blue-500">
            <path stroke-linecap="round" stroke-linejoin="round" d="M17.25 6.75 22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m7.5-3-4.5 16.5" />
          </svg>
        </button>
      </div>

      <!-- 右侧：用户头像 -->
      <div class="flex items-center gap-4 ">
        <!--  加上了 :  -->
        <img :src="options.data.avator" alt="User Avatar" class="h-8 rounded-full hover:scale-110  active:scale-90" />
        <ThemeSwitch/>
      </div>
    </div>

  </nav>
  <main class="basic-color mt-24">
    <router-view/>
  </main>
</template>


<style scoped>

/* 你可以在这里添加一些自定义样式 */
</style>