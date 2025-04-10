<script setup>
import {reactive, ref} from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {useThemeStore} from "@/stores/theme.js";
import {message} from "ant-design-vue";
import {post} from "@/net/index.js";
import axios from "axios";
import {BaseUrl} from "@/main.js";
const [messageApi, contextHolder] = message.useMessage();
const themeStore=useThemeStore();

const text = ref('Start your writing');
const blogForm = reactive({
  title:"",
  content:""
})

const submitHandler=()=>{
  if(!blogForm.title || !blogForm.content || !selectedButton.value) {
    messageApi.warning('请填写完整信息！');
    return;
  }
  post("api/blog/addNew",{
    title:blogForm.title,
    content:blogForm.content,
    tag:selectedButton.value
  },(message)=>{
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  },(message)=>{
    messageApi.error(message);
  })
}

const selectedButton = ref(null);
const selectButton = (button) => {
  selectedButton.value = button;
}

const tags = [
  { name: '算法', icon: '🧠', color: 'from-blue-400 to-cyan-400' },
  { name: '项目', icon: '🚀', color: 'from-purple-400 to-pink-400' },
  { name: '小知识', icon: '💡', color: 'from-green-400 to-emerald-400' }
]

const onUploadImg = async (files, callback) => {
  const res = await Promise.all(
      files.map((file) => {
        return new Promise((resolve, reject) => {
          const form = new FormData();
          form.append('image', file);
          axios.post("api/blog/uploadImg", form, {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": `${localStorage.getItem("authToken")}`,
            },
          })
              .then((response) => resolve(response))
              .catch((error) => reject(error));
        });
      })
  );

  // 处理后端返回的响应
  const fileUrls = res.map((item) => {
    if (item.data.success) {
      // 如果上传成功，返回完整的文件 URL
      //return `https://www.xxx.xyz/api/${item.data.data}`;
      return BaseUrl+`${item.data.data}`;
    } else {
      // 如果上传失败，抛出错误
      throw new Error(item.data.message);
    }
  });

  // 调用回调函数，传递文件的 URL 列表
  callback(fileUrls);
};
</script>

<template>
  <contextHolder/>
  <div class="w-4/5 mx-auto min-h-screen flex flex-col">
    <!-- 顶部操作栏 -->
    <div class="mt-10 sticky top-0 z-10 bg-white/90 dark:bg-purple-900/10 backdrop-blur-md py-4 border-b border-gray-100 dark:border-purple-700/20 ">
      <div class="flex justify-between items-center">
        <div class="flex items-center gap-8">
          <div class="relative w-96 group">
            <input 
              v-model="blogForm.title" 
              class="w-full p-4 text-xl bg-transparent border-b-2 border-gray-200 dark:border-purple-200/30 focus:border-blue-400 dark:focus:border-purple-200/50 outline-none transition-all duration-300 font-light pl-12 text-gray-800 dark:text-purple-200/80"
              placeholder="给你的文章起个有趣的标题..."
            >
            <div class="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 dark:text-purple-200/60 group-hover:text-blue-400 dark:group-hover:text-purple-200/80 transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </div>
            <div class="absolute bottom-0 left-0 w-0 h-0.5 bg-gradient-to-r from-blue-400 to-purple-400 dark:from-purple-200/50 dark:to-purple-200/30 transition-all duration-500" :class="{'w-full': blogForm.title}"></div>
          </div>
          
          <div class="flex gap-4">
            <div 
              v-for="tag in tags" 
              :key="tag.name"
              @click="selectButton(tag.name)"
              class="group cursor-pointer"
            >
              <div 
                class="flex items-center gap-2 px-4 py-2 rounded-lg transition-all duration-300 hover:shadow-md"
                :class="[
                  selectedButton === tag.name 
                    ? `bg-gradient-to-r ${tag.color} text-white shadow-lg dark:from-purple-200/30 dark:to-purple-200/20` 
                    : 'bg-white dark:bg-purple-200/10 border border-gray-200 dark:border-purple-200/30 hover:border-blue-400 dark:hover:border-purple-200/50 text-gray-600 dark:text-purple-200/80'
                ]"
              >
                <span class="text-xl transform group-hover:scale-110 transition-transform duration-300">{{ tag.icon }}</span>
                <span class="text-base font-medium">{{ tag.name }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <button 
          @click="submitHandler" 
          class="flex items-center gap-2 px-6 py-2 bg-gradient-to-r from-blue-400 to-purple-400 dark:from-purple-200/30 dark:to-purple-200/20 text-white dark:text-purple-200/90 rounded-lg shadow-lg hover:shadow-xl transform hover:scale-105 transition-all duration-300 font-medium group"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 transform group-hover:rotate-180 transition-transform duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
          发布文章
        </button>
      </div>
    </div>

    <!-- 编辑器区域 -->
    <div class="flex-1 mt-6">
      <div class="rounded-xl shadow-lg overflow-hidden bg-white dark:bg-purple-200/10 h-full border border-gray-100 dark:border-purple-200/20">
        <MdEditor
            @onUploadImg="onUploadImg"
          v-model="blogForm.content" 
          class="h-full"
          :theme="themeStore.isDark ? 'dark' : 'light'"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
input::placeholder {
  color: #CBD5E1;
  transition: all 0.3s ease;
}

input:focus::placeholder {
  transform: translateY(-20px);
  font-size: 0.8em;
  color: #60A5FA;
}

/* 添加平滑滚动效果 */
* {
  scroll-behavior: smooth;
}

/* 标签动画效果 */
.group:hover .text-gray-600 {
  color: #3B82F6;
}

.group:hover .border-gray-200 {
  border-color: #60A5FA;
}

/* 添加页面加载动画 */
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

.animate-fade-in {
  animation: fadeIn 0.5s ease-out forwards;
}

/* 暗色模式样式 */
.dark .bg-white {
  @apply dark:bg-purple-200/10;
}

.dark .border-gray-100 {
  @apply dark:border-purple-200/20;
}

.dark .text-gray-600 {
  @apply dark:text-purple-200/80;
}

.dark .border-gray-200 {
  @apply dark:border-purple-200/30;
}

.dark .bg-gradient-to-r {
  @apply dark:from-purple-200/20 dark:to-purple-200/10;
}

/* 输入框暗色模式样式 */
.dark input::placeholder {
  @apply dark:text-purple-200/40;
}

.dark input:focus::placeholder {
  @apply dark:text-purple-200/60;
}
</style>