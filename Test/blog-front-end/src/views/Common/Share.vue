<script setup>
import {onMounted, reactive, ref, onUnmounted} from 'vue';
import { MdPreview, MdCatalog} from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import {useThemeStore} from "@/stores/theme.js";
import {message} from "ant-design-vue";
import {get, post} from "@/net/index.js";
import {useRoute} from "vue-router";
import {userUserStore} from "@/stores/userStore.js";
import router from "@/router/index.js";
const [messageApi, contextHolder] = message.useMessage();

const userStore=userUserStore();
const route=useRoute();
const pid=route.params.id;
const id = 'preview-only';
const options=reactive({
  data:{

  }
})
const text = ref('# Hello Editor');
const scrollElement = document.documentElement;
const themeStore=useThemeStore();
const scrollTop = ref(0);
const previewRef = ref();
const catalogList = ref([]);

const onScroll = () => {
  scrollTop.value = scrollElement.scrollTop;
};

const onGetCatalog = (list) => {
  catalogList.value = list;
};

const scrollTo = (index) => {
  const preview = previewRef.value;
  if (preview) {
    const headings = preview.querySelectorAll('h1, h2, h3, h4, h5, h6');
    if (headings[index]) {
      headings[index].scrollIntoView({ behavior: 'smooth' });
    }
  }
};

const fetchBlogContent=()=>{
  get('api/blog/share',
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
  if(userStore.user===null)
  {
    messageApi.warning("尚未登陆不能点赞blog，请先登陆！")
    setTimeout(function () {
      router.push('/auth')
    },1000)
  }
  else
  {
    get("api/blog/checkLike", {
      id:pid
    },(message,data)=>{
      if(userStore.user.role==="游客")
      {
        messageApi.warning("当前身份为游客，不能点赞！")
      }
      else if(data===0)
      {
        likeHandler();
      }
      else {
        unlikeHandler();
      }
    })
  }
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
  if(userStore.user===null)
  {
    messageApi.warning("尚未登陆不能收藏blog，请先登陆！")
    setTimeout(function () {
      router.push('/auth')
    },1000)
  }
  else
  {
    get("api/blog/checkFavourite", {
      id:pid
    },(message,data)=>{
      if(userStore.user==='')
      {
        messageApi.warning("尚未登陆不能点赞，不能点赞！")
        router.push('/auth')
      }
      else if(userStore.user.role==="游客")
      {
        messageApi.warning("当前身份为游客，不能收藏！")
      }
      else if(data===0)
      {
        favouriteHandler();
      }
      else {
        unfavouriteHandler();
      }
    })
  }
}

onMounted(()=>{
  fetchBlogContent();
  window.addEventListener('scroll', onScroll);
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll);
});
</script>

<template>
  <contextHolder/>
  <div class="flex flex-wrap max-h-screen max-w-screen overflow-y-scroll bg-gray-50 dark:bg-purple-800/10 scrollbar-hide">
    <!-- 主内容区域 -->
    <div class="w-2/3 blog-w-full items-center justify-center font-kai gap-4 h-fit rounded-xl p-10 mt-6 bg-white dark:bg-black shadow-lg ml-16">
      <!-- 标题区域 -->
      <div class="hidden-on-small-screens">
        <h1 class="text-5xl font-bold text-gray-800 dark:text-gray-200 px-2 leading-tight">{{options.data.title}}</h1>
        <div class="flex flex-wrap  ">
          <div class="flex items-center  px-4  rounded-lg">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <span class="text-gray-700 dark:text-gray-200">未知用户</span>
          </div>
<!--          <div class="flex items-center    rounded-lg">-->
<!--            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">-->
<!--              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />-->
<!--            </svg>-->
<!--            <span class="text-gray-700 dark:text-gray-200">{{parseDateTime(options.data.blog.updateDate)}}</span>-->
<!--          </div>-->
          <div class="flex items-center  px-4  rounded-lg">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
            <span class="text-gray-700 dark:text-gray-200">{{options.data.isPublic === 0 ? '公开':'私密'}}</span>
          </div>
        </div>
      </div>

      <!-- 互动区域 -->
      <div class="flex items-center  ">
        <button class="flex items-center gap-2 hover:scale-110 active:scale-90 transition-transform  px-4 py-2 rounded-lg" @click="checkLikeHandler">
          <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isLike===true?'#F40909FF': 'none'" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-500">
            <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
          </svg>
          <span class="text-blue-500 font-medium">{{options.data.likes}}</span>
        </button>

        <div class="flex items-center gap-2  px-4 py-2 rounded-lg">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-500">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
          </svg>
          <span class="text-blue-500 font-medium">{{options.data.watches}}</span>
        </div>

        <button class="flex items-center gap-2 hover:scale-110 active:scale-90 transition-transform  px-4 py-2 rounded-lg" @click="checkFavouriteHandler">
          <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isFavourite===true?'#edb20a': 'none'" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-500">
            <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
          </svg>
          <span class="text-blue-500 font-medium">{{options.data.favourites}}</span>
        </button>
      </div>

      <!-- 内容区域 -->
      <div class="prose max-w-none " ref="previewRef">
        <MdPreview
            :theme="themeStore.currentTheme"
            :editorId="id"
            :modelValue="options.data.content"
            :scrollElement="previewRef"
            @onScroll="onScroll"
            @onGetCatalog="onGetCatalog"
        />
      </div>
    </div>

    <!-- 目录区域 -->
    <div class="w-1/4 fixed right-0 top-28 mt-0 bg-white dark:bg-purple-200/10 rounded-xl p-6 shadow-lg hidden md:block" style="max-height: calc(100vh - 200px);">
      <div class="text-2xl font-bold text-blue-600 mb-6 flex items-center border-b-2 border-blue-100 pb-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" />
        </svg>
        目录
      </div>
      <div class="catalog-list">
        <div
            v-for="(item, index) in catalogList"
            :key="index"
            class="catalog-item dark:text-white"
            :class="{ 'active': item.active }"
            :style="{ paddingLeft: `${item.level * 16}px` }"
            @click="scrollTo(index)"
        >
          {{ item.text }}
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
  -ms-overflow-style: none; /*-ms-overflow-style: none;: 这个属性用于隐藏 IE 和 Edge 浏览器中的滚动条。*/
  scrollbar-width: none;  /*scrollbar-width: none;: 这个属性用于隐藏 Firefox 浏览器中的滚动条。*/
}
@media (max-width: 1048px) {
  .blog-w-full {
    width: 100%
  }
  .hidden-on-small-screens {
    display: none;
  }
}


/* 自定义目录样式 */
.catalog-list {
  max-height: calc(100vh - 300px);
  overflow-y: auto;
  padding: 0.5rem;
  margin-bottom: 1rem;
}

.catalog-item {
  padding: 8px 12px;
  margin: 4px 0;
  border-radius: 8px;
  cursor: pointer;
  transition: color 0.3s ease;

  font-size: 15px;
  position: relative;
  border-left: 2px solid transparent;
}

.catalog-item:hover {
  color: #3B82F6;
}

.catalog-item.active {
  color: #3B82F6;
  font-weight: 600;
  border-left: 2px solid #3B82F6;
  background-color: #F8FAFC;
  @apply dark:bg-purple-200/20;
}

.catalog-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 60%;
  background-color: #3B82F6;
  border-radius: 0 4px 4px 0;
}

/* 优化滚动条样式 */
.catalog-list::-webkit-scrollbar {
  width: 4px;
}

.catalog-list::-webkit-scrollbar-track {
  background: #F1F5F9;
  border-radius: 2px;
}

.catalog-list::-webkit-scrollbar-thumb {
  background: #CBD5E1;
  border-radius: 2px;
}

.catalog-list::-webkit-scrollbar-thumb:hover {
  background: #94A3B8;
}


</style>





