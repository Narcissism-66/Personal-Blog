<template>
  <contextHolder/>
  <div class="flex items-center justify-center h-fit">
    <div class="bg-white dark:bg-gray-900  p-8 rounded-lg shadow-md text-blue-300 w-full">
      <h2 class="text-2xl font-semibold mb-6 text-center text-blue-300 select-none cursor-pointer">添加网页链接</h2>
      <div class="mb-4">
        <label for="linkName" class="dark:text-blue-300 block text-sm font-medium text-gray-700">链接名称</label>
        <input
            type="text"
            id="linkName"
            v-model="options.name"
            class="dark:bg-gray-900 mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="例如：Google"
        />
      </div>
      <div class="mb-4">
        <label for="linkUrl" class="dark:text-blue-300 block text-sm font-medium text-gray-700">链接 URL</label>
        <input
            type="url"
            id="linkUrl"
            v-model="options.location"
            class="dark:bg-gray-900 mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="例如：https://www.google.com"
            @blur="validateUrl">
<!--        在失去焦点时调用 validateUrl 方法。-->

        <p v-if="errorMessage" class="text-red-500 text-sm mt-1">{{ errorMessage }}</p>
<!--        如果 errorMessage 有值，则显示错误提示信息。-->
      </div>
      <button
          @click="submitForm"
          class="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        提交
      </button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { post } from "@/net/index.js";
import { message } from "ant-design-vue";
import router from "@/router/index.js";

const [messageApi, contextHolder] = message.useMessage();

const options = reactive({
  name: "",
  location: ''
});
const cleanoptions=()=>{
  options.name = "";
  options.location = "";
  errorMessage.value = "";
}

const errorMessage = ref('');


//        定义 validateUrl 方法，用于验证 URL 是否合法，并根据验证结果设置 errorMessage。
//        const urlPattern = /^(https?|ftp):\/\/([^\s/?#]+)([/?#][^\s]*)?$/i;:
//        这是一个正则表达式，用于匹配 URL 格式。
//        ^(https?|ftp): 匹配以 http、https 或 ftp 开头的协议。
//        :\/\/: 匹配 ://。
//        ([^\s/?#]+): 匹配主机名部分，确保它不是一个空白字符，并且不包含 /、?、#。
//        ([/?#][^\s]*)?: 匹配路径、查询字符串和片段标识符部分，允许 URL 末尾有斜杠。
//        i: 忽略大小写。
const validateUrl = () => {
  const urlPattern = /^(https?|ftp):\/\/([^\s/?#]+)([/?#][^\s]*)?$/i;
  if (!urlPattern.test(options.location)) {
    errorMessage.value = '请输入有效的 URL';
  } else {
    errorMessage.value = '';
  }
};

const submitForm = () => {
  post("api/link/addNew", {
    name: options.name,
    location: options.location,
  }, (message) => {
    messageApi.success(message+"两秒后为您清空数据~");
    setTimeout(function() {
      cleanoptions();
    }, 2000)
  }, (message) => {
    messageApi.warning(message);
  }, (message) => {
    messageApi.error(message);
  });
};
</script>

<style scoped>
/* 你可以在这里添加一些自定义样式 */
</style>