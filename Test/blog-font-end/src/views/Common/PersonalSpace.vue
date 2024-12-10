<script setup>
import {userUserStore} from "@/stores/userStore.js";
import {onMounted, reactive, ref, watch} from "vue";
import {get, post} from "@/net/index.js";
import {parseDateTime} from "@/util/DateFormattor.js";
import router from "@/router/index.js";
import {message} from "ant-design-vue";
import {MdEditor, MdPreview} from "md-editor-v3";
import 'md-editor-v3/lib/style.css';
const [messageApi, contextHolder] = message.useMessage();

const userStore=userUserStore();
const options =reactive({
  selected:{
    BlogType:2
  },
  currentBlog:[],
  BlogById:{
    content:"",
    title:"",
    id:""
  },
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
const IsDelete=ref(false);
const IsDeleteInformation=ref(false);

const CheckIsDelete=(blogId)=>{
  IsDelete.value=!IsDelete.value;
  if(IsDelete.value)
  {
    GetBlogById(blogId);
  }
}
const GetBlogById=(blogId)=>{
  get('api/blog/getBlog',{
    id:blogId
  },(message,data)=>{
    options.BlogById=data;
    // console.log(options.BlogById.blog.content)
    // messageApi.success(message);
  })
}
const DeleteBlog=(blogId)=>{
  post("api/blog/deleteBlog",{
    blogId:blogId
  },(message,data)=>{
    messageApi.success(message);
    AuthorBlogs();
  },(message,data)=>{
    messageApi.warning(message);
  })
}
//是否更新
const Update=reactive({
  title:"",
  text:"",
  blogId:0,
})
const IsUpdate=ref(false);

const CheckIsUpdate=(blog)=>{
  IsUpdate.value=!IsUpdate.value;
  if(IsUpdate.value)
  {
    Update.title=blog.title;
    Update.text=blog.content;
    Update.blogId=blog.id;
    selectedButton.value=blog.tag;
  }
}
const UpdateBlog=()=>{
  post("api/blog/UpdateBlog",{
    title:Update.title,
    content:Update.text,
    blogId:Update.blogId,
    tag:selectedButton.value
  },(message)=>{
    AuthorBlogs();
    messageApi.success(message);
  })
}
const Information=reactive({
  username:userStore.user.username,
  mail:userStore.user.mail,
  password:userStore.user.password
})
const IsUpdateInformation=ref(false);
const CheckIsUpdateInformation=()=>{
  console.log(Information.password)
  IsUpdateInformation.value=!IsUpdateInformation.value;
}
const UpdateInformation=()=>{

}
// 定义一个变量来存储当前选中的按钮
const selectedButton = ref(null);

// 点击按钮时更新 selectedButton
const selectButton = (button) => {
  selectedButton.value = button;
}
onMounted(()=>{
  console.log(options.currentBlog.length)
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
  <contextHolder/>
  <div class="grid grid-cols-[2fr,6fr] h-screen gap-10">
<!--    个人信息+功能-->
    <div class="mx-auto items-center w-2/3 justify-center mt-24 ">
      <div class=" border-blue-200 border-4 rounded-xl p-1  flex flex-wrap gap-2">
        <img :src=userStore.user.avator alt="User Avatar" class="object-cover w-60 h-60  mx-auto justify-center items-center rounded-xl p-1"/>
        <div class="font-kai text-black border-blue-200 border-4 text-xl rounded-xl w-full">
          <h1  class="dark:text-white select-none cursor-pointer">个人信息：
            <span @click="CheckIsUpdateInformation" class="hover:underline text-green-600">修改</span>
          </h1>
          <div class="select-none cursor-pointer w-fit p-2 dark:text-white rounded-xl">用户名：{{userStore.user.username}}</div>
          <div class="select-none cursor-pointer w-fit p-2 dark:text-white rounded-xl">邮箱：{{userStore.user.mail}}</div>
        </div>
        <div class="font-kai text-black border-blue-200 border-4 text-xl rounded-xl w-full">
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

<!--    修改消息的界面-->
    <div v-if="IsUpdateInformation" class="fixed inset-0 flex flex-nowrap items-center justify-center bg-black bg-opacity-50 z-50">
      <div  class="z-50 relative bg-white bg-opacity-75 w-80 h-fit mt-20 p-4 flex flex-wrap gap-4 rounded-xl items-center justify-center mx-auto" >
        <div class="text-3xl text-red-500 mx-auto justify-center items-center w-full font-kai">修改信息</div>
        <svg @click="CheckIsUpdateInformation" xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" class="size-7 hover:scale-110 active:scale-90 hover:text-red-500 hc-transition absolute  right-1 top-2" >
          <path fill="currentColor" d="m8.4 17l3.6-3.6l3.6 3.6l1.4-1.4l-3.6-3.6L17 8.4L15.6 7L12 10.6L8.4 7L7 8.4l3.6 3.6L7 15.6zm3.6 5q-2.075 0-3.9-.788t-3.175-2.137T2.788 15.9T2 12t.788-3.9t2.137-3.175T8.1 2.788T12 2t3.9.788t3.175 2.137T21.213 8.1T22 12t-.788 3.9t-2.137 3.175t-3.175 2.138T12 22m0-2q3.35 0 5.675-2.325T20 12t-2.325-5.675T12 4T6.325 6.325T4 12t2.325 5.675T12 20m0-8"/>
        </svg>
        <div class="font-kai text-xl text-black flex flex-nowrap  mr-auto gap-8">
          <span class="mt-2">头像：</span>
          <img :src="userStore.user.avator" class="h-10 w-10 object-cover rounded-full " alt="头像">
        </div>
        <div class="font-kai text-xl text-black flex flex-nowrap gap-4 h-fit">
          <div class=" w-24">名称：</div>
          <input type="text" v-model="Information.username" class="border-2 border-gray-300 rounded-md  h-fit w-full">
        </div>
        <div class="font-kai text-xl text-black flex flex-nowrap gap-4 h-fit">
          <div class=" w-24">密码：</div>
          <input type="text" v-model="Information.password" class="border-2 border-gray-300 rounded-md  h-fit w-full">
        </div>
        <div class="font-kai text-xl text-black flex flex-nowrap gap-4 h-fit">
          <div class=" w-24">邮箱：</div>
          <input type="text" v-model="Information.mail" class="border-2 border-gray-300 rounded-md  h-fit w-full">
        </div>
        <button class="mx-auto justify-center items-center text-xl font-kai px-4 py-2 bg-blue-300 h-fit w-fit rounded-xl">修改</button>
      </div>
    </div>


    <!--      主内容-->
    <div class="mt-14 p-10 w-full  overflow-y-scroll ">
<!--      没有发表文章-->
      <div v-if="options.currentBlog.length===0" >
        <div class="font-kai ml-32 mt-24 text-4xl text-red-500  h-20">你还没有写过任何文章！</div>
        <button @click="router.push('/edit')" class="font-kai ml-60 text-xl text-black hover:underline">
          去写一篇,试试？
        </button>
      </div>


<!--      将整个 div 包裹在一个 group 类中，这样内部的子元素可以通过 group-hover 来响应父元素的悬停状态。-->
      <div @click="blogClickHandle(blog.id)" v-for="blog in options.currentBlog" class="  w-5/6 select-none cursor-pointer gap-2 p-1 hc-transition hover:scale-105 active:scale-100 group">
        <div class="shadow-xl flex flex-wrap p-2 h-fit w-full gap-4 bg-gray-100 rounded-xl bg-opacity-5 relative">
          <div class="font-kai text-xl text-blue-500 w-full">{{blog.title}}</div>
          <div>
            <div class="flex flex-nowrap gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="#fd6442" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-red-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
              </svg>
              <div class="text-blue-400">{{blog.likes}}</div>
              <svg xmlns="http://www.w3.org/2000/svg" fill="#ffe178" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-yellow-600">
                <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
              </svg>
              <div class="text-blue-400">{{blog.favourites}}</div>
              <svg xmlns="http://www.w3.org/2000/svg" fill="#70584e" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-blue-300">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
              </svg>
              <div class="text-blue-400">{{blog.watches}}</div>
            </div>
          </div>
          <div v-if="options.selected.BlogType===2" class="font-kai text-blue-400">更新时间：{{ parseDateTime(blog.updateDate) }}</div>
          <div v-else-if="options.selected.BlogType===1" class="font-kai text-blue-400">收藏时间：{{ parseDateTime(blog.updateDate) }}</div>
          <div v-if="options.selected.BlogType===0" class="font-kai text-blue-400">点赞时间：{{ parseDateTime(blog.updateDate) }}</div>

          <!-- 修改和删除按钮 -->
          <div v-if="blog.authorId===userStore.user.id" class="absolute flex flex-wrap top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-1000">
            <button @click.stop="CheckIsUpdate(blog)" class="bg-blue-500 text-white px-2 py-1 rounded-md mr-2 font-kai">修改</button>
            <button @click.stop="CheckIsDelete(blog.id)" class="bg-red-500 text-white px-2 py-1  rounded-md font-kai">删除</button>
          </div>
        </div>
      </div>
    </div>
<!--    删除界面-->
    <div v-if="IsDelete" class="fixed inset-0 flex flex-nowrap items-center justify-center bg-black bg-opacity-50 z-50">
      <div  class="relative h-4/5 w-2/3 bg-gray-400 items-center justify-center mx-auto mt-16 ml-64 p-4 rounded-xl bg-opacity-50">
        <div class="flex flex-wrap">
          <div class="text-3xl font-kai text-black mx-auto justify-center items-center">删除提示！</div>
          <MdPreview :editorId="preview-only" :modelValue="options.BlogById.blog.content" class=" w-full overflow-y-auto rounded-xl mt-1" style="max-height: 500px;" />
          <svg @click="CheckIsDelete" xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" class="size-8 hover:scale-110 active:scale-90 hover:text-red-500 hc-transition absolute right-2 top-2" >
            <path fill="currentColor" d="m8.4 17l3.6-3.6l3.6 3.6l1.4-1.4l-3.6-3.6L17 8.4L15.6 7L12 10.6L8.4 7L7 8.4l3.6 3.6L7 15.6zm3.6 5q-2.075 0-3.9-.788t-3.175-2.137T2.788 15.9T2 12t.788-3.9t2.137-3.175T8.1 2.788T12 2t3.9.788t3.175 2.137T21.213 8.1T22 12t-.788 3.9t-2.137 3.175t-3.175 2.138T12 22m0-2q3.35 0 5.675-2.325T20 12t-2.325-5.675T12 4T6.325 6.325T4 12t2.325 5.675T12 20m0-8"/>
          </svg>
          <button @click="DeleteBlog(options.BlogById.blog.id)"  class="w-fit  absolute left-2 top-2 p-2 rounded-xl hover:text-red-500 font-kai  bg-gray-400 bg-opacity-50"  >删除</button>
        </div>
      </div>
    </div>
<!--    修改界面-->
    <div v-if="IsUpdate" class="fixed inset-0 flex flex-nowrap items-center justify-center bg-black bg-opacity-50 z-50">
      <div  class=" h-4/5 w-2/3 bg-gray-400 items-center justify-center mx-auto mt-16 ml-64 p-4 rounded-xl bg-opacity-50">
        <div class="flex">
          <div class="flex flex-nowrap text-2xl font-kai h-fit  w-4/5 gap-4 ">
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
          <input v-model="Update.title" class="animatedInput w-full" placeholder="Your Title Here">
          <button class="rounded-full p-1  flex flex-nowrap" >
            <svg  xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-9 text-black hover:text-red-500 hover:scale-110  active:scale-90" @click="UpdateBlog">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-9 text-black hover:text-red-500 hover:scale-110  active:scale-90" @click="CheckIsUpdate">
              <path stroke-linecap="round" stroke-linejoin="round" d="m9.75 9.75 4.5 4.5m0-4.5-4.5 4.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
          </button>
        </div>
        <MdEditor  class="h-4/5 mt-2 " v-model="Update.text"/>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>