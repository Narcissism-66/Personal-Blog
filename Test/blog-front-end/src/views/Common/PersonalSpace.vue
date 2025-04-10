<script setup>
import {userUserStore} from "@/stores/userStore.js";
import {onMounted, reactive, ref, watch, computed} from "vue";
import {get, post} from "@/net/index.js";
import {parseDateTime} from "@/util/DateFormattor.js";
import router from "@/router/index.js";
import {message} from "ant-design-vue";
import {MdEditor, MdPreview} from "md-editor-v3";
import 'md-editor-v3/lib/style.css';
import axios from "axios";
import {BaseUrl} from "@/main.js";
const [messageApi, contextHolder] = message.useMessage();

const userStore=userUserStore();
const options =reactive({
  avatar:userStore.user.avator===''?"https://tse1-mm.cn.bing.net/th/id/OIP-C.LGIfDSzfoEQmoXOtLcINdAHaHa?w=197&h=198&c=7&r=0&o=5&dpr=1.3&pid=1.7":userStore.user.avator,
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

// 添加主题计算属性
const editorTheme = computed(() => {
  return document.documentElement.classList.contains('dark') ? 'dark' : 'light';
});

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

// 添加搜索相关的状态
const searchQuery = ref('');
const filteredBlogs = computed(() => {
  if (!searchQuery.value) return options.currentBlog;
  const query = searchQuery.value.toLowerCase();
  return options.currentBlog.filter(blog => 
    blog.title.toLowerCase().includes(query)
  );
});

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


//-------------------------------更新头像------------------------------
const isUploadingAvatar=ref(false);
const UploadAvatar=()=>{
  isUploadingAvatar.value=!isUploadingAvatar.value
}

const UpdateAvatar=()=>{
  post("api/user/UpdateAvatar",{
    avatar:options.avatar
  },(message)=>{
    messageApi.success("更新成功！")
  })
}

// 头像上传处理
const handleAvatarUpload = async (e, callback) => {
  const file = e.target.files[0]; // 获取上传的第一个文件
  if (!file) return; // 如果没有选择文件，则退出

  const form = new FormData();
  form.append('image', file);

  try {
    const response = await axios.post("api/user/uploadImg", form, {
      headers: {
        "Content-Type": "multipart/form-data",
        "Authorization": `${localStorage.getItem("authToken")}`,
      },
    });

    if (response.data.success) {
      // 上传成功后，更新头像 URL
       options.avatar = BaseUrl+"api/"+`${response.data.data}`;
      console.log("上传成功，新的头像地址：", options.avatar);
      UpdateAvatar();
      return BaseUrl+`${response.data.data}`;
    } else {
      throw new Error(response.data.message); // 如果上传失败，抛出错误
    }
  } catch (error) {
    console.error("上传失败：", error);
    callback([]); // 上传失败时传递空的 URL 列表
  }
};
</script>

<template>
  <contextHolder/>
  <div class="min-h-screen ">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- 个人信息卡片 -->
      <div class="bg-white dark:bg-purple-200/10 rounded-2xl shadow-sm p-6 mb-8">
        <div class="flex items-center space-x-6">
          <div class="relative">
            <img :src="options.avatar" alt="User Avatar"
                 class="w-24 h-24 rounded-full object-cover border-4 border-blue-100 dark:border-purple-200/20 hover:border-blue-300 dark:hover:border-purple-300/30 transition-all duration-300"/>
            <label class="absolute bottom-0 right-0 bg-blue-500 dark:bg-purple-400 rounded-full p-1.5 cursor-pointer hover:bg-blue-600 dark:hover:bg-purple-500 transition-all duration-300"
                 @click="UploadAvatar">
              <input
                type="file"
                accept="image/*"
                class="hidden"
                @change="handleAvatarUpload"
              />
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
              </svg>
            </label>
            <div v-if="isUploadingAvatar" class="absolute inset-0 flex items-center justify-center bg-black/50 rounded-full">
              <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-white"></div>
            </div>
          </div>
          <div class="flex-1">
            <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-2">{{userStore.user.username}}</h2>
            <p class="text-gray-600 dark:text-gray-400 mb-1">邮箱：{{userStore.user.mail}}</p>
          </div>
        </div>
      </div>

      <!-- 功能导航和搜索 -->
      <div class="flex items-center justify-between mb-8">
        <!-- 功能导航 -->
        <div class="bg-white dark:bg-purple-200/10 rounded-2xl shadow-sm p-6 flex-1 mr-4">
          <div class="grid grid-cols-3 gap-4">
            <div v-for="element in siderBar" 
                 @click="buttonClickHandler(element.id)"
                 :class="[
                   'flex items-center justify-center space-x-2 p-4 rounded-xl cursor-pointer transition-all duration-300',
                   options.selected.BlogType === element.id 
                     ? 'bg-blue-50 dark:bg-purple-200/20 text-blue-600 dark:text-purple-400' 
                     : 'hover:bg-gray-50 dark:hover:bg-purple-200/20 text-gray-600 dark:text-gray-400'
                 ]">
              <div v-html="element.ico" class="w-6 h-6"></div>
              <span class="font-medium">{{element.title}}</span>
            </div>
          </div>
        </div>

        <!-- 搜索框 -->
        <div class="bg-white dark:bg-purple-200/10 rounded-2xl shadow-sm p-6 w-80">
          <div class="relative">
            <input 
              v-model="searchQuery"
              type="text"
              placeholder="搜索文章标题..."
              class="w-full px-4 py-2 border border-gray-200 dark:border-purple-200/20 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-purple-400 focus:border-transparent transition-all duration-300 bg-white dark:bg-purple-200/10 text-gray-900 dark:text-gray-200"
            />
            <div class="absolute right-3 top-1/2 transform -translate-y-1/2">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400 dark:text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="space-y-6">
        <!-- 空状态 -->
        <div v-if="filteredBlogs.length===0" 
             class="bg-white dark:bg-purple-200/10 rounded-2xl shadow-sm p-8 text-center">
          <div v-if="searchQuery" class="text-gray-500 dark:text-gray-400 text-lg mb-4">没有找到相关文章</div>
          <div v-else class="text-gray-500 dark:text-gray-400 text-lg mb-4">你还没有写过任何文章！</div>
          <button @click="router.push('/edit')" 
                  class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-xl text-white bg-blue-500 dark:bg-purple-400 hover:bg-blue-600 dark:hover:bg-purple-500 transition-all duration-300">
            开始创作
          </button>
        </div>

        <!-- 文章列表 -->
        <div v-else class="grid gap-6">
          <div v-for="blog in filteredBlogs" 
               class="bg-white dark:bg-purple-200/10 rounded-2xl shadow-sm hover:shadow-md transition-all duration-300">
            <div class="p-6">
              <div class="flex justify-between items-start mb-4">
                <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-200 cursor-pointer" @click="blogClickHandle(blog.id)">{{blog.title}}</h3>
                <div v-if="blog.authorId===userStore.user.id" 
                     class="flex space-x-3">
                  <button @click.stop="CheckIsUpdate(blog)" 
                          class="p-2 text-blue-500 dark:text-purple-400 hover:text-blue-600 dark:hover:text-purple-500 rounded-lg hover:bg-blue-50 dark:hover:bg-purple-200/20 transition-all duration-300">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                    </svg>
                  </button>
                  <button @click.stop="CheckIsDelete(blog.id)" 
                          class="p-2 text-red-500 dark:text-red-400 hover:text-red-600 dark:hover:text-red-500 rounded-lg hover:bg-red-50 dark:hover:bg-red-200/20 transition-all duration-300">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </div>
              
              <div class="flex items-center space-x-6 text-gray-500 dark:text-gray-400 mb-4">
                <div class="flex items-center space-x-1">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="#fd6442" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
                  </svg>
                  <span>{{blog.likes}}</span>
                </div>
                <div class="flex items-center space-x-1">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="#ffe178" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
                  </svg>
                  <span>{{blog.favourites}}</span>
                </div>
                <div class="flex items-center space-x-1">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="#70584e" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                  </svg>
                  <span>{{blog.watches}}</span>
                </div>
              </div>

              <div class="text-sm text-gray-500 dark:text-gray-400">
                <span v-if="options.selected.BlogType===2">更新时间：{{ parseDateTime(blog.updateDate) }}</span>
                <span v-else-if="options.selected.BlogType===1">收藏时间：{{ parseDateTime(blog.updateDate) }}</span>
                <span v-if="options.selected.BlogType===0">点赞时间：{{ parseDateTime(blog.updateDate) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改界面 -->
    <div v-if="IsUpdate" class="fixed inset-0 flex items-center justify-center bg-black/30 backdrop-blur-sm z-50">
      <div class="bg-white dark:bg-purple-200/10 rounded-2xl p-4 w-[800px] h-[80vh] flex flex-col shadow-lg">
        <div class="flex-none">
          <div class="flex justify-between items-center mb-3">
            <h2 class="text-lg font-semibold text-gray-800 dark:text-gray-200">修改文章</h2>
            <button @click="CheckIsUpdate" class="text-gray-400 dark:text-gray-500 hover:text-gray-600 dark:hover:text-gray-400 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          
          <div class="flex space-x-2 mb-2">
            <button
              v-for="tag in ['算法', '项目', '小知识']"
              :key="tag"
              @click="selectButton(tag)"
              class="px-2 py-1 rounded-md transition-all duration-300 text-xs"
              :class="selectedButton === tag ? 'bg-blue-500 dark:bg-purple-400 text-white' : 'bg-gray-100 dark:bg-purple-200/20 text-gray-600 dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-purple-200/30'"
            >
              {{tag}}
            </button>
          </div>

          <input
              v-model="Update.title"
              class="w-full px-2 py-1 border border-gray-200 dark:border-purple-200/20 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-purple-400 focus:border-transparent transition-all duration-300 mb-2 text-sm bg-white dark:bg-purple-200/10 text-gray-900 dark:text-gray-200"
              placeholder="输入文章标题"
          />
        </div>

        <div class="flex-1 min-h-0 rounded-md border border-gray-200 dark:border-purple-200/20 mb-2">
          <MdEditor @onUploadImg="onUploadImg" v-model="Update.text" style="height: 100%;" :theme="editorTheme" />
        </div>
        
        <div class="flex-none flex justify-end space-x-2">
          <button @click="CheckIsUpdate" 
                  class="px-2 py-1 text-gray-600 dark:text-gray-400 hover:text-gray-800 dark:hover:text-gray-300 transition-all duration-300 text-xs">
            取消
          </button>
          <button @click="UpdateBlog" 
                  class="px-2 py-1 bg-blue-500 dark:bg-purple-400 text-white rounded-md hover:bg-blue-600 dark:hover:bg-purple-500 transition-all duration-300 text-xs">
            保存修改
          </button>
        </div>
      </div>
    </div>

    <!-- 删除界面 -->
    <div v-if="IsDelete" class="fixed inset-0 flex items-center justify-center bg-black/30 backdrop-blur-sm z-50">
      <div class="bg-white dark:bg-purple-200/10 rounded-2xl p-4 w-[800px] h-[80vh] flex flex-col shadow-lg">
        <div class="flex-none">
          <div class="flex justify-between items-center mb-3">
            <h2 class="text-lg font-semibold text-gray-800 dark:text-gray-200">确认删除</h2>
            <button @click="CheckIsDelete" class="text-gray-400 dark:text-gray-500 hover:text-gray-600 dark:hover:text-gray-400 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          
          <h3 class="text-lg font-medium text-gray-700 dark:text-gray-300 mb-2">{{options.BlogById.blog.title}}</h3>
        </div>

        <div class="flex-1 min-h-0 rounded-md border border-gray-200 dark:border-purple-200/20 mb-2 overflow-auto p-4" style="scrollbar-width: none; -ms-overflow-style: none;">
          <MdPreview :editorId="preview-only" :modelValue="options.BlogById.blog.content" :theme="editorTheme" />
        </div>
        
        <div class="flex-none flex justify-end space-x-2">
          <button @click="CheckIsDelete" 
                  class="px-2 py-1 text-gray-600 dark:text-gray-400 hover:text-gray-800 dark:hover:text-gray-300 transition-all duration-300 text-xs">
            取消
          </button>
          <button @click="DeleteBlog(options.BlogById.blog.id)" 
                  class="px-2 py-1 bg-red-500 dark:bg-red-400 text-white rounded-md hover:bg-red-600 dark:hover:bg-red-500 transition-all duration-300 text-xs">
            确认删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}

.group:hover .group-hover\:opacity-100 {
  opacity: 1;
}

.hc-transition {
  transition: all 0.3s ease;
}

.animatedInput {
  @apply w-full p-2 rounded-lg border-2 border-gray-200 focus:border-blue-500 focus:outline-none transition-all duration-300;
}

.animatedInput:focus {
  @apply shadow-lg;
}
</style>