<script setup>
import {onMounted, reactive, ref} from "vue";
import {get, post} from "@/net/index.js";
import {useRoute} from "vue-router";
import {message} from "ant-design-vue";
import router from "@/router/index.js";
import {userUserStore} from "@/stores/userStore.js";
const [messageApi, contextHolder] = message.useMessage();

const userStore=userUserStore();
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
  if(userStore.user.username==="222")
  {
    messageApi.warning("当前身份为游客，不能评论！")
  }
  else{
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
  if(userStore.user.username==="222")
  {
    messageApi.warning("当前身份为游客，不能回复！")
  }
  else if(type===1){
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
  <div class="comment-section">
    <div class="comment-input-wrapper">
      <div class="input-container">
        <input 
          v-model="newReview.question" 
          class="comment-input" 
          placeholder="分享你的想法..."
        >
        <button 
          @click="ReviewBlogsHandler" 
          class="send-button"
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="send-icon">
            <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5">
              <path d="M20.394 20.75a2.34 2.34 0 0 0 2.3-2.8l-2.3-11.483a4 4 0 0 0-3.923-3.217H4.62a1.5 1.5 0 0 0-1.464 1.825L4.4 10.684a2 2 0 0 0 1.954 1.566h4.566a4 4 0 0 1 3.772 2.669l1.292 3.66a3.25 3.25 0 0 0 3.069 2.171z"/>
              <path d="M11.769 12.34a6.23 6.23 0 0 1-.569 3.56a.6.6 0 0 0 .552.854h3.588M19.594 4.75h2.156a1.5 1.5 0 0 1 1.5 1.5v1.5a1.5 1.5 0 0 1-1.5 1.5h-.8m-16.532 1.5H2.25a1.5 1.5 0 0 1-1.5-1.5v-3a1.5 1.5 0 0 1 1.5-1.5h.87m11.13 3a1.5 1.5 0 0 1-1.5 1.5h-3.5a1.5 1.5 0 0 1 0-3h3.5a1.5 1.5 0 0 1 1.5 1.5"/>
            </g>
          </svg>
        </button>
      </div>
    </div>

    <div class="comments-wrapper">
      <div class="comments-title">
        <span class="title-text">评论区</span>
        <span class="title-decoration">💭</span>
      </div>

      <div class="comments-list">
        <div v-for="review in options.reviews" :key="review.reviews.id" class="comment-item">
          <div v-if="review.reviews.replyId===0" class="comment-card">
            <div class="comment-header">
              <div class="user-info">
                <div class="avatar-wrapper">
                  <img :src=review.reviews.authoravator alt="avatar" class="user-avatar">
                </div>
                <div class="comment-content">
                  <div class="comment-text items-center justify-center my-auto">{{ review.reviews.question }}</div>
                  <div class="comment-actions">
                    <button class="action-button" @click="toggleReview(review.reviews.id)">
                      <span class="action-icon">💬</span>
                      <span class="action-text">回复</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="activeReviewId === review.reviews.id" class="reply-input-wrapper">
              <div class="input-container">
                <input
                  v-model="reviewReplies[review.reviews.id].question"
                  class="reply-input"
                  :placeholder="placeholder"
                >
                <button
                  @click="submitReply(review.reviews.authorId, review.reviews.id, review.reviews.id,1)"
                  class="submit-button"
                >
                  发送
                </button>
              </div>
            </div>

            <div class="replies-list">
              <div v-for="reply in options.replys" :key="reply.id" class="reply-item">
                <div v-if="reply.replyId===review.reviews.id" class="reply-card">
                  <div class="reply-header">
                    <div class="user-info">
                      <div class="avatar-wrapper">
                        <img :src=reply.useravator alt="avatar" class="user-avatar">
                      </div>
                      <div class="reply-content">
                        <div class="reply-meta">
                          <span class="username">{{reply.username}}</span>
                          <span class="reply-icon">→</span>
                          <span class="username">{{reply.authorname}}</span>
                        </div>
                        <div class="reply-text">{{reply.question}}</div>
                        <div class="reply-actions">
                          <button class="action-button" @click="toggleReply(reply.id)">
                            <span class="action-icon">💬</span>
                            <span class="action-text">回复</span>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div v-if="activeReplyId === reply.id" class="reply-input-wrapper">
                    <div class="input-container">
                      <input
                        v-model="replyReplies[reply.id].question"
                        class="reply-input"
                        :placeholder="placeholder"
                      >
                      <button 
                        @click="submitReply(reply.authorId,review.reviews.id,reply.id,2)" 
                        class="submit-button"
                      >
                        发送
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
  </div>
</template>

<style scoped>
.comment-section {
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem;
}

.comment-input-wrapper {
  margin-bottom: 2rem;
}

.input-container {
  display: flex;
  gap: 0.5rem;
  background: #fff;
  border-radius: 12px;
  padding: 0.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comment-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 0.75rem;
  font-size: 0.95rem;
  background: transparent;
}

.send-button {
  background: #3b82f6;
  border: none;
  border-radius: 8px;
  padding: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.send-button:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

.send-icon {
  width: 24px;
  height: 24px;
  color: white;
}

.comments-wrapper {
  background: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comments-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  font-size: 1.25rem;
  color: #1e293b;
}

.title-text {
  font-weight: 600;
}

.title-decoration {
  font-size: 1.2em;
  animation: float 2s ease-in-out infinite;
}

.comment-card {
  margin-bottom: 1.5rem;
  animation: fadeIn 0.3s ease-out;
}

.comment-header {
  margin-bottom: 1rem;
}

.user-info {
  display: flex;
  gap: 1rem;
}

.avatar-wrapper {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.comment-text {
  font-size: 0.95rem;
  line-height: 1.5;
  color: #1e293b;
  margin-bottom: 0.5rem;
  flex: 1;
  margin-right: 1rem;
}

.comment-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  background: none;
  border: none;
  color: #64748b;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  white-space: nowrap;
}

.action-button:hover {
  background: #f1f5f9;
  color: #3b82f6;
}

.action-icon {
  font-size: 1.1em;
}

.replies-list {
  margin-left: 3rem;
}

.reply-card {
  background: #f8fafc;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
  animation: slideIn 0.3s ease-out;
}

.reply-meta {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  font-size: 0.85rem;
  color: #64748b;
}

.username {
  color: #3b82f6;
  font-weight: 500;
}

.reply-icon {
  color: #94a3b8;
}

.reply-text {
  font-size: 0.9rem;
  line-height: 1.5;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.reply-input-wrapper {
  margin-top: 1rem;
}

.reply-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 0.75rem;
  font-size: 0.9rem;
  background: #f8fafc;
  border-radius: 8px;
}

.submit-button {
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1rem;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-button:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

.dark .input-container,
.dark .comments-wrapper,
.dark .comment-card,
.dark .reply-card {
  background: #1e293b;
}

.dark .comment-input,
.dark .reply-input {
  background: transparent;
  color: #e2e8f0;
}

.dark .comment-text,
.dark .reply-text {
  color: #e2e8f0;
}

.dark .action-button:hover {
  background: #334155;
}

.dark .reply-card {
  background: #334155;
}

.dark .comments-title {
  color: #e2e8f0;
}

.dark .reply-meta {
  color: #94a3b8;
}
</style>