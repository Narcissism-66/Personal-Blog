<script setup>
import {onMounted, reactive} from "vue";
import {get, post} from "@/net/index.js";
import {parseDateTime} from "@/util/DateFormattor.js";
import {message} from "ant-design-vue";
const [messageApi, contextHolder] = message.useMessage();

const options =reactive({
  selected:{
    notificationType:0
  },currentNotification:[],
})
const buttonClickHandler=(buttonId)=>{
  options.selected.notificationType=buttonId;
  fetchNotificationByType(buttonId);
}
const fetchNotificationByType=(type)=>{
  get("api/user/getNotifications",{
    type:type
  },(message,data)=>{
    options.currentNotification=data;
  })
}
const readNotification=(id)=>{
  post("api/user/readNotification",{
    id:id
  },(message)=>{
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}

const clearNotification=(type)=>{
  post("api/user/clearNotifications",{
    type:type
  },(message)=>{
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}
onMounted(()=>{
  fetchNotificationByType(0);
})
const sideBarObjects = [
  {
    typeId: 0,
    name: "点赞",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
</svg>
`,
    id:0,
  },
  {
    typeId:1,
    name:"关注",
    svg:`<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
</svg>
`,
    id:1,
  },
  {
    typeId:2,
    name:"评论",
    svg:`<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 0 1-2.555-.337A5.972 5.972 0 0 1 5.41 20.97a5.969 5.969 0 0 1-.474-.065 4.48 4.48 0 0 0 .978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25Z" />
</svg>
`,
    id:2,
  },
  {
    typeId:3,
    name:"系统",
    svg:`<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M7.217 10.907a2.25 2.25 0 1 0 0 2.186m0-2.186c.18.324.283.696.283 1.093s-.103.77-.283 1.093m0-2.186 9.566-5.314m-9.566 7.5 9.566 5.314m0 0a2.25 2.25 0 1 0 3.935 2.186 2.25 2.25 0 0 0-3.935-2.186Zm0-12.814a2.25 2.25 0 1 0 3.933-2.185 2.25 2.25 0 0 0-3.933 2.185Z" />
</svg>
`,
    id:3,
  }
]
</script>

<template>
  <contextHolder/>
  <div class="h-full mx-auto grid grid-cols-[2fr,7fr] gap-4 ">
    <!-- 侧边栏 -->
<!--    点击侧边栏按钮后，options.selected.notificationType 会被更新为点击按钮的 id，-->
<!--    然后通过 fetchNotificationByType 函数获取对应类型的通知数据，并更新 options.currentNotification。-->
<!--    由于 options.currentNotification 是响应式的，Vue 会自动重新渲染模板，显示对应类型的通知内容。-->
<!--    通过修改 options.selected.notificationType 来获取不同的 options.currentNotification 是实现点击按钮后显示对应内容的核心机制-->
    <div class="h-fit mt-24">
      <div class="flex flex-nowrap mx-auto items-center justify-center">
        <div class="font-kai text-2xl p-4 cursor-pointer select-none flex flex-nowrap text-black justify-center rounded-xl  w-fit dark:text-white dark:hover:text-blue-300">
          消息中心
        </div>
        <div class="h-4/5 w-fit hover:scale-110 active:scale-90" @click="clearNotification(0)">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-7 dark:text-white text-black hover:text-blue-300 dark:hover:text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12c0 1.268-.63 2.39-1.593 3.068a3.745 3.745 0 0 1-1.043 3.296 3.745 3.745 0 0 1-3.296 1.043A3.745 3.745 0 0 1 12 21c-1.268 0-2.39-.63-3.068-1.593a3.746 3.746 0 0 1-3.296-1.043 3.745 3.745 0 0 1-1.043-3.296A3.745 3.745 0 0 1 3 12c0-1.268.63-2.39 1.593-3.068a3.745 3.745 0 0 1 1.043-3.296 3.746 3.746 0 0 1 3.296-1.043A3.746 3.746 0 0 1 12 3c1.268 0 2.39.63 3.068 1.593a3.746 3.746 0 0 1 3.296 1.043 3.746 3.746 0 0 1 1.043 3.296A3.745 3.745 0 0 1 21 12Z" />
          </svg>
        </div>
      </div>
      <div
          :class="{
          'hover:scale-110 active:scale-90': true,
          'text-red-300': options.selected.notificationType === element.id,
          'dark:text-blue-300': options.selected.notificationType === element.id,
          'dark:hover:text-blue-300': true,
          'active:text-red-500': true,
        }"
          @click="buttonClickHandler(element.id)"
          v-for="element in sideBarObjects"
          class="gap-2 p-4 cursor-pointer select-none flex hover:scale-110 active:scale-90 flex-nowrap text-black hover:text-blue-300 justify-center active:text-blue-500  rounded-xl mx-auto w-fit dark:text-white "
      >
        <div v-html="element.svg"></div>
        <h1 class="font-kai text-xl">{{ element.name }}</h1>
        <!-- 使用v-if来控制SVG的显示 -->
        <div v-if="options.selected.notificationType === element.id">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
          </svg>
        </div>
      </div>
    </div>
    <!-- 主内容区域 -->
    <div class=" h-screen overflow-y-scroll ">
      <div class="flex flex-wrap p-4 mt-24">
        <div class=" shadow-xl select-none border-2 border-green-200 dark:border-gray-700 mt-2 p-2 w-full h-fit rounded-xl" v-for="notification in options.currentNotification">
          <h1 class="text-blue-500 font-bold font-kai text-2xl">{{notification.title}}</h1>
          <div class=" text-black dark:text-blue-50">{{notification.content}}</div>
          <div class="text-gray-400 font-kai">{{parseDateTime(notification.createTime)}}</div>
          <span @click="readNotification(notification.id);notification.isRead=true" v-if="!notification.isRead" class="text-black dark:text-white dark:hover:text-blue-200  font-kai hover:text-blue-300 hover:underline font-bold ">点击已读</span>
        </div>
      </div>
    </div>
  </div>
</template>
