<script setup>
import {useThemeStore} from "@/stores/theme.js";
import router from "@/router/index.js";
import {userUserStore} from "@/stores/userStore.js";
import {message} from "ant-design-vue";
import {ref} from "vue";
import {post} from "@/net/index.js";
const [messageApi, contextHolder] = message.useMessage();
const themeStore=useThemeStore();
const userStore=userUserStore();

const role=ref(userStore.user.role);
const toggleThemeHandler=()=>{
  themeStore.toggleTheme();
}
const Qualification_blog=()=>{
  if(userStore.user.role==="游客")
  {
    messageApi.warning("当前身份为游客，不能发布文章")
  }
  else {
    router.push('/edit')
  }
}
const Qualification_notification=()=>{
  if(userStore.user.role==="游客")
  {
    messageApi.warning("当前身份为游客，不能查看消息")
  }
  else {
    router.push('/notification')
  }
}

//更新为离线
const UpdateOnline = () => {
  post('api/user/UpdateOnline', {
    userId: userStore.user.id,
    isOnline: "false"
  }, (message) => {
    messageApi.success("离线啦~")
  });
};
const logout=()=>{
  UpdateOnline();
  userStore.logout();
  router.push('/auth')
}
</script>

<template>
  <contextHolder/>
<!--  信息-->
<!--  <button v-if="userStore.user.readCount!==0 && role!=='游客'" class=" animate-shake relative "  @click="Qualification_notification">-->
<!--    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 hover:scale-110 active:scale-90 text-red-500">-->
<!--      <path stroke-linecap="round" stroke-linejoin="round" d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0M3.124 7.5A8.969 8.969 0 0 1 5.292 3m13.416 0a8.969 8.969 0 0 1 2.168 4.5" />-->
<!--    </svg>-->
<!--    <span class="absolute top-0 right-0 w-2 h-2 bg-red-500 rounded-full "></span>-->
<!--  </button>-->
<!--  <button v-else class=" relative "  @click="Qualification_notification">-->
<!--    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-black hover:scale-110 active:scale-90 dark:text-red-300">-->
<!--      <path stroke-linecap="round" stroke-linejoin="round" d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0M3.124 7.5A8.969 8.969 0 0 1 5.292 3m13.416 0a8.969 8.969 0 0 1 2.168 4.5" />-->
<!--    </svg>-->
<!--  </button>-->

<!--  记笔记-->
<!--  <button>-->
<!--    <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" class="size-5 text-black hover:scale-110  active:scale-90" @click="router.push('/note')">-->
<!--      <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="M13.4 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-7.4M2 6h4m-4 4h4m-4 4h4m-4 4h4"/><path d="M21.378 5.626a1 1 0 1 0-3.004-3.004l-5.01 5.012a2 2 0 0 0-.506.854l-.837 2.87a.5.5 0 0 0 .62.62l2.87-.837a2 2 0 0 0 .854-.506z"/></g>-->
<!--    </svg>-->
<!--  </button>-->

<!-- 发布-->
  <button class="hover:scale-110  active:scale-90 bg-[#fe785a] flex flex-nowrap p-1 rounded-xl hover:bg-red-500 active:bg-red-600 " @click="Qualification_blog">
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5">
      <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
    </svg>
    <span class="font-bold font-kai ">发布</span>
  </button >

  <button>
    <svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 26 26" class="size-5 text-black hover:scale-110  active:scale-90" @click="router.push('/plan')">
      <path fill="currentColor" d="M7.875 0a1 1 0 0 0-.656.375L3.812 4.656l-2.25-1.5A1.014 1.014 0 1 0 .438 4.844l3 2a1 1 0 0 0 1.344-.219l4-5A1 1 0 0 0 7.875 0M12 3v2h14V3zM7.875 9a1 1 0 0 0-.656.375l-3.407 4.281l-2.25-1.5a1.014 1.014 0 1 0-1.125 1.688l3 2a1 1 0 0 0 1.344-.219l4-5A1 1 0 0 0 7.875 9M12 12v2h14v-2zm-4.125 6a1 1 0 0 0-.656.375l-3.407 4.281l-2.25-1.5a1.014 1.014 0 1 0-1.125 1.688l3 2a1 1 0 0 0 1.344-.219l4-5A1 1 0 0 0 7.875 18M12 21v2h14v-2z"/>
    </svg>
  </button>

<!--  管理按钮（控制台）-->
  <button  v-if="role==='管理员'||role==='创始人'" class=" rounded-xl p-0.5 hover:scale-110  active:scale-90" @click="router.push('/admin')">
    <svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 24 24" class="size-6 text-black hover:text-blue-500  active:scale-90 ">
      <g fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="9" r="2"/><path d="M16 15c0 1.105 0 2-4 2s-4-.895-4-2s1.79-2 4-2s4 .895 4 2Z"/><path stroke-linecap="round" d="M3 10.417c0-3.198 0-4.797.378-5.335c.377-.537 1.88-1.052 4.887-2.081l.573-.196C10.405 2.268 11.188 2 12 2s1.595.268 3.162.805l.573.196c3.007 1.029 4.51 1.544 4.887 2.081C21 5.62 21 7.22 21 10.417v1.574c0 2.505-.837 4.437-2 5.913M3.193 14c.857 4.298 4.383 6.513 6.706 7.527c.721.315 1.082.473 2.101.473c1.02 0 1.38-.158 2.101-.473c.579-.252 1.231-.58 1.899-.994"/></g>
    </svg>
  </button>

  <!--  退出-->
  <button class="hover:scale-110  active:scale-90" @click="logout">
    <svg  xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-black hover:text-blue-500  active:scale-90" >
      <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0 0 13.5 3h-6a2.25 2.25 0 0 0-2.25 2.25v13.5A2.25 2.25 0 0 0 7.5 21h6a2.25 2.25 0 0 0 2.25-2.25V15m3 0 3-3m0 0-3-3m3 3H9" />
    </svg>
  </button>


</template>

<style>
@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: translateX(-2px);
  }
  20%, 40%, 60%, 80% {
    transform: translateX(2px);
  }
}

.animate-shake {
  animation: shake 0.8s ease-in-out infinite;
}
</style>
