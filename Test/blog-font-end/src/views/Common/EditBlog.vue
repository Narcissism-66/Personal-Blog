<script setup>
import {reactive, ref} from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {useThemeStore} from "@/stores/theme.js";

import {message} from "ant-design-vue";
import {post} from "@/net/index.js";
import axios from "axios";
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
// 定义一个变量来存储当前选中的按钮
const selectedButton = ref(null);

// 点击按钮时更新 selectedButton
const selectButton = (button) => {
  selectedButton.value = button;
}

// const onUploadImg=async (files,callback)=>{
//   const res=await Promise.all(
//       files.map((file)=>{
//         return new Promise((rev,rej)=>{
//           const form=new FormData();
//           form.append('image',file);
//           axios.post("api/user/uploadImg",form,{
//             headers:{
//               "Content-Type":"multipart/form-data",
//               "Authorization":`${localStorage.getIntem("authToken")}`,
//             },
//               })
//              .then((res)=>rev(res))
//              .catch((error)=>rej(error));
//         });
//     })
//   );
//   callback(res.map((item)=> fatherBackLink + item.data.data.path))
// }
</script>

<template>
  <contextHolder/>
  <div class="w-4/5 mx-auto max-h-4/5  ">
    <div class="flex ">
      <input v-model="blogForm.title" class="animatedInput  mt-24 w-full" placeholder="Your Title Here">
      <button class="rounded-full p-1 " @click="submitHandler" >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="hover:scale-110  active:scale-90 hc-transition size-9 hover:text-blue-500 mt-24">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
        </svg>
      </button>
      <div class="flex flex-nowrap text-2xl font-kai h-fit mt-24 w-1/2 gap-4 ">
        <button
            class="bg-blue-300 bg-opacity-50 px-7 rounded-xl p-1 hover:scale-110  active:scale-90 hc-transition"
            :class="{ 'bg-blue-500': selectedButton === '算法' }"
            @click="selectButton('算法')"
        >
          算法
        </button>
        <button
            class="bg-blue-300 bg-opacity-50 px-7 rounded-xl p-1 hover:scale-110  active:scale-90 hc-transition"
            :class="{ 'bg-blue-500': selectedButton === '项目' }"
            @click="selectButton('项目')"
        >
          项目
        </button>
        <button
            class="bg-blue-300 bg-opacity-50 px-7 rounded-xl p-1 hover:scale-110  active:scale-90 hc-transition"
            :class="{ 'bg-blue-500': selectedButton === '小知识' }"
            @click="selectButton('小知识')"
        >
          小知识
        </button>
      </div>
    </div>
    <MdEditor @onUploadImg="onUploadImg"  class=" mt-2 " v-model="blogForm.content"  />
  </div>
</template>