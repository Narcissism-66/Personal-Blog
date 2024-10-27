<script setup>
import {userUserStore} from "@/stores/userStore.js";
import {onMounted, reactive} from "vue";
import {get} from "@/net/index.js";
import {parseDateTime} from "@/util/DateFormattor.js";
import router from "@/router/index.js";
import {message} from "ant-design-vue";

const userStore=userUserStore();
const options =reactive({
  selected:{
    BlogType:2
  },currentBlog:[],
})
const buttonClickHandler=(buttonId)=>{
  options.selected.BlogType=buttonId;
  if(buttonId===0) BlogsLikedByUserId();
  else if(buttonId===2)AuthorBlogs();
  else BlogsFavouriteByUserId();
}
const BlogsLikedByUserId=()=>{
  get("api/blog/getBlogsLikedByUserId",{}, (message,data)=>{
    options.currentBlog=data;
  })
}
const BlogsFavouriteByUserId=()=>{
  get("api/blog/getBlogsFavouritesByUserId",{}, (message,data)=>{
    options.currentBlog=data;
  })
}
const AuthorBlogs=()=>{
  get('/api/blog/getBlogsByAuthorId',{},(message,data)=>{
    options.currentBlog=data;
  })
}
onMounted(()=>{
  AuthorBlogs();
})
const blogClickHandle=(id)=>{
  router.push("/view/"+id)
}
const siderBar=[{
  title:'点赞',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" fill="#fd6442" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
</svg>
`,
  id:0,
},{
  title:'收藏',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" fill="#ffe178" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
</svg>
`,
  id:1
},{
  title:'我的文章',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" fill="#ff942d" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
</svg>
`,
  id:2
}];
</script>

<template>
  <div class="grid grid-cols-[2fr,6fr,2fr] h-screen gap-4">
<!--    个人信息+功能-->
    <div class=" p-14 ">
      <div class=" border-gary-200 border-4 rounded-xl p-1 dark:border-gray-800 ">
        <img :src=userStore.user.avator alt="User Avatar" class="w-30 h-30 rounded-xl p-1"/>
        <div class="font-kai text-black border-gray-200 border-4 text-xl rounded-xl dark:border-gray-800">
          <h1  class="dark:text-white select-none cursor-pointer">个人信息：</h1>
          <div class="select-none cursor-pointer w-fit p-2 dark:text-white rounded-xl">用户名：{{userStore.user.username}}</div>
          <div class="select-none cursor-pointer w-fit p-2 dark:text-white rounded-xl">邮箱：{{userStore.user.mail}}</div>
        </div>
        <div class="font-kai text-black border-gray-200 border-4 text-xl rounded-xl dark:border-gray-800">
          <h1 class="dark:text-white select-none cursor-pointer">功能：</h1>
          <div @click="buttonClickHandler(element.id)" v-for="element in siderBar" class="hc-transition cursor-pointer select-none flex hover:scale-110 active:scale-90 flex-nowrap text-black hover:text-blue-300  active:text-blue-500  rounded-xl  w-fit dark:text-white dark:hover:text-blue-300">
            <div v-html="element.ico"></div>
            <h1 class="font-kai text-xl">{{element.title}}</h1>
            <div v-if="options.selected.BlogType === element.id">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
              </svg>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="mt-8">
<!--      主内容-->
      <div @click="blogClickHandle(blog.id)" v-for="blog in options.currentBlog" class=" select-none cursor-pointer gap-4 p-4 hc-transition hover:scale-105 active:scale-100">

        <div class="shadow-xl flex flex-wrap p-2 h-fit w-full gap-4 dark:bg-gray-800 bg-gray-100 rounded-xl">
          <div class="font-kai text-xl text-blue-300 w-full">{{blog.title}}</div>
          <div >
            <div class="flex flex-nowrap gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="#fd6442" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-red-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
              </svg>
              <div class="text-blue-300 ">{{blog.likes}}</div>
              <svg xmlns="http://www.w3.org/2000/svg" fill="#ffe178" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-yellow-600">
                <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
              </svg>
              <div class="text-blue-300 ">{{blog.favourites}}</div>
              <svg xmlns="http://www.w3.org/2000/svg" fill="#70584e" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-blue-300">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
              </svg>
              <div class="text-blue-300 ">{{blog.watches}}</div>
            </div>
          </div>
          <div v-if="options.selected.BlogType===2" class="font-kai text-blue-300">更新时间：{{ parseDateTime(blog.updateDate) }}</div>
          <div v-else-if="options.selected.BlogType===1" class="font-kai text-blue-300">收藏时间：{{ parseDateTime(blog.updateDate) }}</div>
          <div v-if="options.selected.BlogType===0" class="font-kai text-blue-300">点赞时间：{{ parseDateTime(blog.updateDate) }}</div>
        </div>

      </div>
    </div>
    <div class="bg-black">
    </div>
  </div>
</template>

<style scoped>

</style>