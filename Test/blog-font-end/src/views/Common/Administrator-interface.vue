<script setup>

import {get, post} from "@/net/index.js";
import {computed, inject, onMounted, reactive, ref, watch, watchEffect} from "vue";
import {userUserStore} from "@/stores/userStore.js";
import {parseDateTime} from "@/util/DateFormattor.js";
import router from "@/router/index.js";
import {message} from "ant-design-vue";
import RegistrationForm from "@/components/Auth/RegistrationForm.vue";
import {UserOnline} from "@/stores/onlineStore.js";
const [messageApi, contextHolder] = message.useMessage();

const manage=ref('用户');
const userStore=userUserStore();

const AllMessage=reactive({
  Users:[],
  blogs:[],
})
const getAllUsers=()=>{
  get('api/user/getAllUsers',{},(message,data)=>{
    AllMessage.Users=data;
  })
}
const getAllBlogs=()=>{
  get('api/blog/getIntroduce',{},(message,data)=>{
    AllMessage.blogs=data;
  })
}
const UserManager=()=> {
  manage.value = "user";
}
const BlogManager=()=> {
  manage.value = "blog";
}
const blogClickHandle=(id)=>{
  router.push("/view/"+id)
}

const DeleteUser=(id)=>{
  post('api/user/deleteUserById',{
    id:id,
  },(message)=>{
    messageApi.success(message+"1秒后关闭此页面");
    setTimeout(() => {
      check.value=0;
    }, 1000);
    getAllUsers();
  })
}
const check=ref(0);
const IsDelete=(id)=>{
  check.value=id;
}

const IsAdd=ref(false);
const IsAddUser=()=>{
  IsAdd.value=!IsAdd.value;
}

const UpdateRole=(id,role)=>{
  post('api/user/updateRole',{
    userId:id,
    role:role
  },(message)=>{
    messageApi.success(message);
    getAllUsers();
  })
}
//每个1秒运行一次
const onlineStore = UserOnline();
const CheckUser=()=>{
  console.log(onlineStore.online)
}
setInterval(CheckUser, 3000);
watch(() =>onlineStore.online, (newValue, oldValue) => {
  if (newValue !== oldValue) {
    getAllUsers();
  }
},{deep:true});


onMounted(()=>{
  getAllUsers();
  getAllBlogs();
  UserManager();
})
</script>

<template>
  <contextHolder/>
  <div class="grid grid-cols-[1fr,7fr] h-screen gap-10">
    <div class="flex flex-wrap  h-screen">
      <button @click="UserManager" class="mt-24 flex flex-nowrap mx-auto justify-center items-center p-2 text-xl font-kai h-fit w-full opacity-50 border-r-2 border-gray-300">
        用户管理
        <svg v-if="manage==='user'" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
        </svg>
      </button>
      <button @click="BlogManager" class="mt-24 flex flex-nowrap mx-auto justify-center items-center p-2 text-xl font-kai h-fit w-full opacity-50 border-r-2 border-gray-300">
        文章管理
        <svg v-if="manage==='blog'" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
        </svg>
      </button>
    </div>

    <div class="flex flex-wrap">
      <!-- 用户管理 -->
      <div class="w-5/6 flex flex-nowrap h-fit mt-20">
        <button v-if="manage==='user'" @click="IsAddUser" class="bg-green-600 bg-opacity-30 hover:bg-opacity-50 transition-colors duration-1000 ease-in-out  rounded-xl p-2 w-1/3  items-center justify-center mx-auto font-kai text-xl">添加用户</button>
      </div>
      <div v-if="IsAdd" class=" fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div  class="absolute items-center justify-center mx-auto ">
          <svg @click="IsAddUser" xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" class="size-7 hover:scale-110 active:scale-90 hover:text-red-500 hc-transition absolute mt-1 " style="margin-left: 365px">
            <path fill="currentColor" d="m8.4 17l3.6-3.6l3.6 3.6l1.4-1.4l-3.6-3.6L17 8.4L15.6 7L12 10.6L8.4 7L7 8.4l3.6 3.6L7 15.6zm3.6 5q-2.075 0-3.9-.788t-3.175-2.137T2.788 15.9T2 12t.788-3.9t2.137-3.175T8.1 2.788T12 2t3.9.788t3.175 2.137T21.213 8.1T22 12t-.788 3.9t-2.137 3.175t-3.175 2.138T12 22m0-2q3.35 0 5.675-2.325T20 12t-2.325-5.675T12 4T6.325 6.325T4 12t2.325 5.675T12 20m0-8"/>
          </svg>
          <RegistrationForm/>
        </div>
      </div>

      <div v-if="manage==='user'" class="w-5/6 h-4/5 mb-10  mt-4 bg-gray-400 bg-opacity-50 gap-4 overflow-hidden rounded-xl">
        <div class=" w-full overflow-y-auto " style="max-height: 600px;">
          <table class=" w-full text-left border-collapse  ">
            <thead>
            <tr class="bg-gray-300 ">
              <th class="p-3 ">ID</th>
              <th class="p-3 ">头像</th>
              <th class="p-3">用户名</th>
              <th class="p-3">邮箱</th>
              <th class="p-3">身份</th>
              <th class="p-3">状态</th>
              <th class="p-3">修改</th>
            </tr>
            </thead>
            <tbody>
            <tr  v-for="users in AllMessage.Users" class="h-fit border-b border-gray-200 ">
              <td v-if="users.role!=='创始人'" class="p-3 h-fit">{{users.id}}</td>
              <td v-if="users.role!=='创始人'" class="p-3 h-fit">
                <img :src="users.avator" alt="头像" class="w-8 h-8 object-cover rounded-full">
              </td>
              <td v-if="users.role!=='创始人'" class="p-3 h-fit">{{users.username}}</td>
              <td v-if="users.role!=='创始人'" class="p-3 h-fit">{{users.mail}}</td>
              <td v-if="users.role!=='创始人'" class="p-3 h-fit text-blue-700">{{users.role}}
                <span class="text-sm ml-4 text-black">
                  设置为
                  <button @click="UpdateRole(users.id,'用户')" v-if="users.role === '管理员'" class=" hover:underline p-2 text-sm text-red-600">用户</button>
                  <button @click="UpdateRole(users.id,'用户')" v-else-if="users.role==='游客'" class=" hover:underline p-2 text-sm text-green-600">用户</button>
                  <span v-else>
                    <button @click="UpdateRole(users.id,'游客')"  class="hover:underline p-2 text-sm text-green-600">游客</button>/
                    <button @click="UpdateRole(users.id,'管理员')"  class="hover:underline p-2 text-sm text-red-600">管理员</button>
                  </span>
                </span>
              </td>
              <td v-if="users.role!=='创始人'" class="p-3 h-fit">
                <span v-if="users.isOnline==='true'" class="text-green-700 text-xl font-kai">在线</span>
                <span v-else class="text-gray-700 text-xl font-kai">离线</span>
              </td>
              <td v-if="users.role!=='创始人'" class="p-3">
                <button @click="IsDelete(users.id)" class="hover:underline  p-2 text-sm text-red-600">删除</button>
              </td>
            </tr>
            <!-- 更多行可以继续添加 -->
            </tbody>
          </table>
<!--          删除提示-->
          <div v-if="check!==0" class=" fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div  class="absolute items-center justify-center mx-auto top-24 w-96 h-96 bg-gray-100 bg-opacity-50 rounded-xl">
              <div class="text-3xl font-kai text-yellow-200 ml-32 mt-10  ">删除提醒！</div>
              <div class="w-2/3 h-fit  ml-32 mt-14 rounded-xl font-kai text-xl">
                你确定要删除
              </div>
              <div class="w-2/3 h-fit text-yellow-200 ml-40 mt-4 rounded-xl font-kai text-2xl">
                id:{{check}}
              </div>
              <div class="w-2/3 h-fit  ml-36 mt-4 rounded-xl font-kai text-xl">
                的用户吗？
              </div>
              <div class="flex flex-nowrap gap-10 ml-16 mt-14">
                <button @click="DeleteUser(check)" class="bg-red-600 rounded-xl p-2 w-1/3">确认</button>
                <button @click="IsDelete(0)" class="bg-green-600 rounded-xl p-2 w-1/3">取消</button>
              </div>
            </div>
          </div>

        </div>
      </div>
      <!-- 文章管理 -->
      <div v-else class="w-5/6   bg-gray-400 bg-opacity-50 mb-10 gap-4 overflow-hidden rounded-xl">
<!--    overflow-y-auto: 给包含表格的 div 元素设置 overflow-y-auto，并指定一个 max-height，这样当内容超过这个高度时，会出现垂直滚动条。-->
        <div class="w-full overflow-y-auto  scrollbar-hide" style="max-height: 580px;">
          <table class="w-full text-left border-collapse">
            <thead>
            <tr class="bg-gray-300 sticky top-0">
              <th class="p-3">ID</th>
              <th class="p-3">标题</th>
              <th class="p-3">作者</th>
              <th class="p-3">更新时间</th>
              <th class="p-3">修改</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="blogs in AllMessage.blogs" class="border-b border-gray-200 text-xl">
              <td class="p-3 font-kai">{{ blogs.blog.id }}</td>
              <td @click="blogClickHandle(blogs.blog.id)" class="p-3 hover:text-red-300 hover:underline font-kai transition-colors duration-1000 ease-in-out cursor-pointer">{{ blogs.blog.title }}</td>
              <td class="p-3 font-kai text-green-700">{{ blogs.username }}</td>
              <td class="p-3 font-kai">{{ parseDateTime(blogs.blog.updateDate) }}</td>
              <td class="p-3">
                <button class="p-2  text-red-600 font-kai text-xl">删除</button>
              </td>
            </tr>
            <!-- 更多行可以继续添加 -->
            </tbody>
          </table>
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
</style>