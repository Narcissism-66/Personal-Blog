<script setup>
import {onMounted, reactive, ref} from "vue";
import {get, post} from "@/net/index.js";
import {useRoute} from "vue-router";
import {message} from "ant-design-vue";
import router from "@/router/index.js";
const [messageApi, contextHolder] = message.useMessage();


const route=useRoute();
const pid=route.params.id;
let activeReplyId=ref("0");
let activeReviewId=ref("0");
const id = 'preview-only';
const options=reactive({
  reviews:[],
  replys:[],
  useriformations:[],
})
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
    reviewsByBlogId();
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
    messageApi.success(message);
    getAllReview();
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
  reviewsByBlogId();
  getAllReview();
})
</script>

<template>
  <contextHolder/>
  <div class="gap-4 ">
    <div class="flex flex-nowrap ">
      <input v-model="newReview.question" class="animatedInput w-full" placeholder="Your Title Here">
      <button @click="ReviewBlogsHandler" class="w-1/6 bg-blue-400 text-white py-4 px-7 rounded-xl hover:bg-blue-700 active:scale-95 active:shadow-lg">
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
</template>

<style scoped>

</style>