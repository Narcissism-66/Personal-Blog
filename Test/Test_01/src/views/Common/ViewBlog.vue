<script setup>
import {onMounted, reactive, ref} from 'vue';
import { MdPreview, MdCatalog } from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import {useThemeStore} from "@/stores/theme.js";
import {message} from "ant-design-vue";
import {get, post} from "@/net/index.js";
import {useRoute} from "vue-router";
import {parseDateTime} from "@/util/DateFormattor.js";
import Review from "@/views/Common/Review.vue";
const [messageApi, contextHolder] = message.useMessage();

const route=useRoute();
const pid=route.params.id;
const id = 'preview-only';
const options=reactive({
  data:{
    blog:{
      content:"",
      title:"",
    }
  }
})
const text = ref('# Hello Editor');
const scrollElement = document.documentElement;
const themeStore=useThemeStore();
const fetchBlogContent=()=>{
  get('api/blog/getBlog',
      {id:pid},
      (message,data)=>{
        options.data=data;
      })
}
const likeHandler=()=>{
  post("api/blog/like",{
    id:pid,
  },(message)=>{
    options.data.blog.likes++;
    options.data.isLike=true;
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}
const unlikeHandler=()=>{
  post("api/blog/unlike",{
    id:pid,
  },(message)=>{
    options.data.blog.likes--;
    options.data.isLike=false;
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}
const checkLikeHandler=()=>{
  get("api/blog/checkLike", {
    id:pid
  },(message,data)=>{
    if(data===0)
    {
      likeHandler();
    }
    else {
      unlikeHandler();
    }
  })
}
const favouriteHandler=()=>{
  post("api/blog/addNewFavourite",{
    blogId:pid,
  },(message)=>{
    options.data.blog.favourites++;
    options.data.isFavourite=true;
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}
const unfavouriteHandler=()=>{
  post("api/blog/unfavourite",{
    blogId:pid,
  },(message)=>{
    options.data.blog.favourites--;
    options.data.isFavourite=false;
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}
const checkFavouriteHandler=()=>{
  get("api/blog/checkFavourite", {
    id:pid
  },(message,data)=>{
    if(data===0)
    {
      favouriteHandler();
    }
    else {
      unfavouriteHandler();
    }
  })
}

onMounted(()=>{
  fetchBlogContent();
})
</script>

<template>
  <contextHolder/>
  <div class="flex max-h-screen ml-64 max-w-screen  overflow-y-scroll">
    <!-- 主内容区域 -->
    <div class="w-4/5 font-kai gap-4 h-fit rounded-xl p-4 mt-24 bg-white">
      <!-- 主内容可以放在这里 -->
      <h1 class="text-blue-300 font-bold rounded-xl w-fit h-auto p-2  text-4xl cursor-pointer select-none">标题：{{options.data.blog.title}}</h1>
      <div class="flex flex-nowrap gap-4 ml-4">
        <h1 class=" font-bold rounded-xl w-fit h-auto  text-xl text-red-500 cursor-pointer select-none">
          作者：{{options.data.username}}</h1>
        <h1 class=" font-bold rounded-xl w-fit h-auto text-xl  text-blue-300 cursor-pointer select-none">
          修改时间：{{parseDateTime(options.data.blog.updateDate)}}</h1>
        <span class="select-none text-xl cursor-pointer" :class="options.data.blog.isPublic === 0 ? 'text-green-600':'text-green-600'">
          查看权限：{{options.data.blog.isPublic === 0 ? '公开':'私密'}}</span>
      </div>

<!--   Blog-->
      <div class="flex font-normal gap-4 ml-4">
        <button class="hover:scale-110 active:scale-90" @click="checkLikeHandler">
          <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isLike===true?'#F40909FF': 'none'" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
          </svg>
        </button>
        <div class="text-xl text-blue-300 select-none cursor-pointer">{{options.data.blog.likes}}</div>
        <button >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
          </svg>
        </button>
        <div class="text-xl text-blue-300 select-none cursor-pointer">{{options.data.blog.watches}}</div>
        <button class="hover:scale-110 active:scale-90" @click="checkFavouriteHandler">
          <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isFavourite===true?'#edb20a': 'none'" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300 ">
            <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
          </svg>
        </button>
        <div class="text-xl text-blue-300 select-none cursor-pointer">{{options.data.blog.favourites}}</div>
        <div v-if="options.data.blog.isPublic !== 0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
          </svg>
        </div>
        <div v-else>
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 10.5V6.75a4.5 4.5 0 1 1 9 0v3.75M3.75 21.75h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H3.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
          </svg>
        </div>
      </div>
      <MdPreview :theme="themeStore.currentTheme" :editorId="id" :modelValue="options.data.blog.content" />
<!--      评论-->
      <Review/>
    </div>
  </div>
</template>





