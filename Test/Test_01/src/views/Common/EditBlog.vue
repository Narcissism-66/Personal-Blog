<script setup>
import {reactive, ref} from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {useThemeStore} from "@/stores/theme.js";

import {message} from "ant-design-vue";
import {post} from "@/net/index.js";
const [messageApi, contextHolder] = message.useMessage();
const themeStore=useThemeStore();
const text = ref('Start your writing');
const blogForm = reactive({
  title:"",
  content:""
})
const submitHandler=()=>{
  post("api/blog/addNew",{
    title:blogForm.title,
    content:blogForm.content
  },(message)=>{
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  },(message)=>{
    messageApi.error(message);
  })
}
</script>

<template>
  <contextHolder/>
  <div class="w-4/5 mx-auto max-h-screen ">
    <div class="flex ">
      <input v-model="blogForm.title" class="animatedInput w-full mt-24" placeholder="Your Title Here">
      <button class="rounded-full p-1 " @click="submitHandler" >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="hover:scale-110  active:scale-90 hc-transition size-9 hover:text-blue-500 mt-24">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
        </svg>
      </button>
    </div>
    <MdEditor  class="h-4/5 mt-2 " v-model="blogForm.content" :theme="themeStore.currentTheme" />
  </div>
</template>