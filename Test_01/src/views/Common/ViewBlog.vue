<script setup>
import {onMounted, reactive, ref} from 'vue';
import { MdPreview, MdCatalog } from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import {useThemeStore} from "@/stores/theme.js";
import {message} from "ant-design-vue";
import {get, post} from "@/net/index.js";
import {useRoute} from "vue-router";
import {parseDateTime} from "@/util/DateFormattor.js";
import Review from "@/views/Common/Review.vue";
const [messageApi, contextHolder] = message.useMessage();

const route=useRoute();
const pid=route.params.id;
let activeReplyId=ref("0");
let activeReviewId=ref("0");
const id = 'preview-only';
const options=reactive({
  data:{
    blog:{
      content:"",
      title:"",
    }
  },
  reviews:[],
  replys:[],
  useriformations:[],
})
const text = ref('# Hello Editor');
const scrollElement = document.documentElement;
const themeStore=useThemeStore();
const fetchBlogContent=()=>{
  get('api/blog/getBlog',
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
  get("api/blog/checkLike", {
    id:pid
  },(message,data)=>{
    if(data===0)
    {
      likeHandler();
    }
    else {
      unlikeHandler();
    }
  })
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
  get("api/blog/checkFavourite", {
    id:pid
  },(message,data)=>{
    if(data===0)
    {
      favouriteHandler();
    }
    else {
      unfavouriteHandler();
    }
  })
}
const newReview = reactive({
  question: "",
  authorId: "",
});
//让每一个评论都有自己的输入框（看不懂）
const reviewReplies = reactive({});
const replyReplies = reactive({});
const initializeReplies = () => {
  options.reviews.forEach(review => {
    Object.assign(reviewReplies, {
      [review.reviews.id]: reactive({
        question: "",
        authorId: "",
      }),
    });
  });

  options.replys.forEach(reply => {
    Object.assign(replyReplies, {
      [reply.id]: reactive({
        question: "",
        authorId: "",
      }),
    });
  });
}

//添加评论
const ReviewBlogsHandler=()=>{
  post("api/blog/addNewReview",{
    blogId:pid,
    question:newReview.question,
    replyId:0,
  },(message)=>{
    messageApi.success(message);
  },(message)=>{
    messageApi.warning(message);
  })
}
//回复评论
//authorId:回复的是谁
//replyId:最上面的评论的Id
//reviewId:每一条评论的Id
const ReplyBlogsHandler=(authorId,replyId,reviewId,type)=> {
  post("api/blog/addNewReply", {
    blogId: pid,
    question: type===1?reviewReplies[reviewId].question:replyReplies[reviewId].question,
    authorId:authorId,
    replyId:replyId,
  }, (message) => {
    console.log(authorId);
    messageApi.success(message);
  }, (message) => {
    messageApi.warning(message);
  })
}
//获取每一个评论里面的所有回复
const getAllReview=()=>{
  get("api/blog/getAllReviewByBlogId",{
    blogId:pid,
  },(message,data)=>{
    options.replys=data;
    initializeReplies();
  });
}
//获取整个Blog的评论
const reviewsByBlogId=()=>{
  get("api/blog/getReviewsByBlogId",{
    blogId:pid,
  },(message,data)=>{
    options.reviews=data;
    initializeReplies();
  })
}
// 打开/关闭回复的输入框
const toggleReply=(id)=> {
  if (activeReplyId.value === id) {
    activeReplyId.value ="0"; // 如果点击的是同一个评论，则关闭输入框
  } else {
    activeReplyId.value = id; // 否则，打开当前点击的评论的输入框
  }
}
// 打开/关闭头评论的输入框
const toggleReview=(id)=> {
  if (activeReviewId.value === id) {
    activeReviewId.value ="0"; // 如果点击的是同一个评论，则关闭输入框
  } else {
    activeReviewId.value = id; // 否则，打开当前点击的评论的输入框
  }
}
//如果输入框是空，就不能执行提交回复/评论
const placeholder = ref('Your Content Here');
const submitReply=(authorId, reviewId, id,type)=> {
  if(type===1){
    if (!reviewReplies[id].question) {
      placeholder.value = 'This field cannot be empty';
      return; // 阻止执行 ReplyBlogsHandler
    }
    placeholder.value = 'Your Content Here'; // 恢复原始 placeholder
    ReplyBlogsHandler(authorId, reviewId, id,1);
  }
  else if(type===2){
    if (!replyReplies[id].question) {
      placeholder.value = 'This field cannot be empty';
      return; // 阻止执行 ReplyBlogsHandler
    }
    console.log(replyReplies[id].question);
    placeholder.value = 'Your Content Here'; // 恢复原始 placeholder
    ReplyBlogsHandler(authorId, reviewId, id,2);
  }

}


onMounted(()=>{
  fetchBlogContent();
  reviewsByBlogId();
  getAllReview();
})
</script>

<template>
  <contextHolder/>
  <div class="flex h-screen" >
    <!-- 目录导航栏 -->
    <aside class="w-1/6  text-white  p-5">
      <MdCatalog :theme="themeStore.currentTheme" :editorId="id" :scrollElement="scrollElement" class="font-kai" />
      <!-- 导航栏内容可以放在这里 -->
    </aside>

    <!-- 主内容区域 -->
    <main class="w-full font-kai gap-4">
      <!-- 主内容可以放在这里 -->
      <h1 class="text-blue-300 font-bold rounded-xl w-fit h-auto p-2  text-4xl cursor-pointer select-none">标题：{{options.data.blog.title}}</h1>
      <div class="flex flex-nowrap gap-4">
        <h1 class=" font-bold rounded-xl w-fit h-auto  text-xl text-red-500 cursor-pointer select-none">
          作者：{{options.data.username}}</h1>
        <h1 class=" font-bold rounded-xl w-fit h-auto text-xl  text-blue-300 cursor-pointer select-none">
          修改时间：{{parseDateTime(options.data.blog.updateDate)}}</h1>
        <span class="select-none text-xl cursor-pointer" :class="options.data.blog.isPublic === 0 ? 'text-green-600':'text-green-600'">
          查看权限：{{options.data.blog.isPublic === 0 ? '公开':'私密'}}</span>
      </div>

<!--   Blog-->
      <div class="flex font-normal gap-4">
        <button class="hover:scale-110 active:scale-90" @click="checkLikeHandler">
          <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isLike===true?'#F40909FF': 'none'" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
          </svg>
        </button>
        <div class="text-xl text-blue-300 select-none cursor-pointer">{{options.data.blog.likes}}</div>
        <button >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
          </svg>
        </button>
        <div class="text-xl text-blue-300 select-none cursor-pointer">{{options.data.blog.watches}}</div>
        <button class="hover:scale-110 active:scale-90" @click="checkFavouriteHandler">
          <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isFavourite===true?'#edb20a': 'none'" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300 ">
            <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
          </svg>
        </button>
        <div class="text-xl text-blue-300 select-none cursor-pointer">{{options.data.blog.favourites}}</div>
        <div v-if="options.data.blog.isPublic !== 0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
          </svg>
        </div>
        <div v-else>
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-blue-300">
            <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 10.5V6.75a4.5 4.5 0 1 1 9 0v3.75M3.75 21.75h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H3.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
          </svg>
        </div>
      </div>
      <MdPreview :theme="themeStore.currentTheme" :editorId="id" :modelValue="options.data.blog.content" />

<!--      评论-->
      <div class="gap-4 ">
        <div class="flex flex-nowrap ">
          <input v-model="newReview.question" class="animatedInput w-full" placeholder="Your Title Here">
          <button @click="ReviewBlogsHandler" class="w-1/6 bg-indigo-600 text-white py-4 px-7 rounded-xl hover:bg-indigo-700 active:scale-95 active:shadow-lg">
            评论
          </button>
        </div>
        <div class="gap-1 rounded-xl border-gray-300 dark:border-gray-700 border-4 flex flex-wrap mt-4 p-2">
          <div class="text-blue-500 text-xl w-full">评论区：</div>
<!--          回复第一个评论-->
          <div v-for="review in options.reviews"  :key="review.reviews.id" class="w-4/5">
            <div v-if="review.reviews.replyId===0" class="border-4 border-gray-300 dark:border-gray-600  rounded-xl">
              <div class=" cursor-pointer p-2 font-kai text-blue-600 dark:text-blue-400 bg-blue-100 dark:bg-gray-700 rounded-xl">
                <div class="flex flex-nowrap gap-2">
                  <img :src=review.reviews.authoravator alt="avatar" class="w-8 rounded-xl">
                  <div class="mt-2">{{ review.reviews.question }}</div>
                  <button class="hc-transition hover:text-red-500 text-blue-300 hover:scale-110 active:scale-90" @click="toggleReview(review.reviews.id)">回复</button>
                </div>
                <div v-if="activeReviewId === review.reviews.id" class="flex flex-nowrap">
                  <input
                      v-model="reviewReplies[review.reviews.id].question"
                      class="animatedInput w-4/5"
                      :placeholder="placeholder"
                  >
                  <button
                      @click="submitReply(review.reviews.authorId, review.reviews.id, review.reviews.id,1)"
                      class="w-hit bg-indigo-600 text-white px-4 py-2 rounded-xl hover:bg-indigo-700 active:scale-95 active:shadow-lg"
                  >
                    回复
                  </button>
                  <br>
                </div>
<!--                展示+回复后面的评论-->
                <div v-for="reply in options.replys"  class="w-3/5 gap-2 ml-6">
                  <div v-if="reply.replyId===review.reviews.id">
                    <div class="cursor-pointer p-2 font-kai text-blue-600 dark:text-blue-400 bg-blue-100 dark:bg-gray-700 rounded-xl">
                      <div class="flex flex-nowrap gap-2">
                        <div>
                          <img :src=reply.useravator alt="avatar" class="w-8 rounded-xl">
                        </div>
                        <div class="gird grid-rows-[1fr,13fr]">
                          <div >
                            <span class="text-blue-300 hover:underline">{{reply.username}}</span> 回复了
                            <span class="text-blue-300 hover:underline">{{reply.authorname}}</span>
                            <button class=" ml-6 hc-transition hover:text-red-500 text-blue-300 hover:scale-110 active:scale-90" @click="toggleReply(reply.id)">回复</button>
                          </div>
                          <div class="mt-2">{{reply.question}}</div>
                        </div>
                      </div>
                      <div v-if="activeReplyId === reply.id" class="flex flex-nowrap">
                        <input
                            v-model="replyReplies[reply.id].question"
                            class="animatedInput w-4/5"
                            :placeholder="placeholder"
                        >
                        <button @click="submitReply(reply.authorId,review.reviews.id,reply.id,2)" class="w-hit bg-indigo-600 text-white px-4 py-2 rounded-xl hover:bg-indigo-700 active:scale-95 active:shadow-lg">
                          回复
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>





