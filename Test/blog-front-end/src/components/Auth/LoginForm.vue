<script setup>
import {reactive, ref} from "vue";
import { message } from 'ant-design-vue';
import {post} from "@/net/index.js";
import router from "@/router/index.js";
import {useThemeStore} from "@/stores/theme.js";

const [messageApi, contextHolder] = message.useMessage();
const themeStore = useThemeStore();
const IsLogin = ref(true);
const emits = defineEmits(['update:test']);

const goAnotherForm = () => {
  router.push("/register");
}

const goHome = () => {
  emits('update:test',3)
}

const loginform = reactive({
  username: "",
  password: ''
})

const loginHandler = () => {
  post('api/auth/login',{
    username:loginform.username,
    password:loginform.password
  },(message,data) => {
    messageApi.success(message)
    localStorage.setItem("authToken",data);
    setTimeout(function() {
      router.push('/')
    }, 1000)
  }, (message, data) => {
    messageApi.warning(message)
  }, (message, data) => {
    messageApi.error("发生错误，请联系管理员~")
  })
}

const checkLoginHandler = () => {
  IsLogin.value = !IsLogin.value;
}
</script>

<template>
  <contextHolder/>
  <div class="login-container">
    <div class="background-animation">
      <div class="gradient-sphere sphere-1"></div>
      <div class="gradient-sphere sphere-2"></div>
      <div class="gradient-sphere sphere-3"></div>
    </div>
    
    <div class="content-wrapper">
      <div class="blog-intro">
        <div class="intro-content">
          <div class="title-wrapper">
            <h1>个人博客</h1>
            <div class="title-decoration"></div>
          </div>
          <p class="intro-text">探索技术的无限可能</p>
          <div class="feature-list">
            <div class="feature-item">
              <div class="feature-icon">📚</div>
              <div class="feature-info">
                <h3>技术文章</h3>
                <p>深度技术解析</p>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">💡</div>
              <div class="feature-info">
                <h3>学习笔记</h3>
                <p>知识沉淀分享</p>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">🎯</div>
              <div class="feature-info">
                <h3>项目展示</h3>
                <p>创新项目实践</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="login-box">
        <div v-if="IsLogin===false" class="login-icon" @click="checkLoginHandler">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="icon">
            <path fill="currentColor" d="M21.5 9h-5l1.86-1.86A7.99 7.99 0 0 0 12 4c-4.42 0-8 3.58-8 8c0 1.83.61 3.5 1.64 4.85c1.22-1.4 3.51-2.35 6.36-2.35s5.15.95 6.36 2.35A7.95 7.95 0 0 0 20 12h2c0 5.5-4.5 10-10 10S2 17.5 2 12S6.5 2 12 2c3.14 0 5.95 1.45 7.78 3.72L21.5 4zM12 20c1.9 0 3.64-.66 5-1.76c-.64-1.01-2.55-1.74-5-1.74s-4.36.73-5 1.74c1.36 1.1 3.1 1.76 5 1.76m0-14c1.93 0 3.5 1.57 3.5 3.5S13.93 13 12 13s-3.5-1.57-3.5-3.5S10.07 6 12 6m0 2c-.83 0-1.5.67-1.5 1.5S11.17 11 12 11s1.5-.67 1.5-1.5S12.83 8 12 8"/>
          </svg>
        </div>
        
        <div v-if="IsLogin===true" class="login-form">
          <div class="form-header">
            <h2>欢迎回来</h2>
            <p class="subtitle">请登录您的账号</p>
          </div>
          
          <div class="form-body">
            <div class="form-group">
              <div class="input-wrapper">
                <input
                    type="text"
                    v-model="loginform.username"
                    placeholder="用户名"
                />
                <div class="input-border"></div>
              </div>
            </div>
            
            <div class="form-group">
              <div class="input-wrapper">
                <input
                    type="password"
                    v-model="loginform.password"
                    placeholder="密码"
                />
                <div class="input-border"></div>
              </div>
            </div>
            
            <button @click="loginHandler()" class="submit-btn">
              <span class="btn-text">登录</span>
              <span class="btn-glow"></span>
            </button>
          </div>
          
          <div class="footer">
            <span>游客账号：222 密码：222</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0a0a0a;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  position: relative;
  overflow: hidden;
}

.background-animation {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.gradient-sphere {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
  animation: float 20s infinite ease-in-out;
}

.sphere-1 {
  width: 600px;
  height: 600px;
  background: radial-gradient(circle at center, #1a73e8 0%, transparent 70%);
  top: -200px;
  left: -200px;
  animation-delay: 0s;
}

.sphere-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle at center, #4285f4 0%, transparent 70%);
  bottom: -150px;
  right: -150px;
  animation-delay: -5s;
}

.sphere-3 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle at center, #34a853 0%, transparent 70%);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation-delay: -10s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(30px, 30px) rotate(90deg);
  }
  50% {
    transform: translate(0, 60px) rotate(180deg);
  }
  75% {
    transform: translate(-30px, 30px) rotate(270deg);
  }
}

.content-wrapper {
  display: flex;
  width: 80%;
  max-width: 1000px;
  margin: 0 40px;
  background: rgba(20, 20, 20, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(26, 115, 232, 0.1);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.blog-intro {
  flex: 1.2;
  padding: 40px 30px;
  background: linear-gradient(135deg, #1a1a1a 0%, #0a0a0a 100%);
  color: #1a73e8;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.title-wrapper {
  position: relative;
  margin-bottom: 20px;
}

.title-decoration {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #1a73e8, transparent);
  border-radius: 3px;
}

.blog-intro h1 {
  font-size: 36px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 2px;
  text-shadow: 0 0 20px rgba(26, 115, 232, 0.3);
  background: linear-gradient(45deg, #1a73e8, #4285f4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.intro-text {
  font-size: 18px;
  opacity: 0.9;
  margin: 0 0 40px;
  letter-spacing: 1px;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px;
  background: rgba(26, 115, 232, 0.05);
  border-radius: 12px;
  border: 1px solid rgba(26, 115, 232, 0.1);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.feature-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, transparent, rgba(26, 115, 232, 0.1), transparent);
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.feature-item:hover::before {
  transform: translateX(100%);
}

.feature-item:hover {
  transform: translateX(10px);
  background: rgba(26, 115, 232, 0.1);
  border-color: rgba(26, 115, 232, 0.2);
  box-shadow: 0 0 20px rgba(26, 115, 232, 0.1);
}

.feature-icon {
  font-size: 28px;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(26, 115, 232, 0.1);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.feature-item:hover .feature-icon {
  transform: scale(1.1) rotate(10deg);
  background: rgba(26, 115, 232, 0.2);
}

.feature-info h3 {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #1a73e8;
}

.feature-info p {
  font-size: 14px;
  color: rgba(26, 115, 232, 0.7);
  margin: 0;
}

.login-box {
  flex: 0.8;
  padding: 40px 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: rgba(30, 30, 30, 0.8);
  position: relative;
}

.login-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(26, 115, 232, 0.05), transparent);
  animation: shimmer 3s infinite;
}

.login-icon {
  display: flex;
  justify-content: center;
  cursor: pointer;
  margin-bottom: 40px;
}

.icon {
  width: 80px;
  height: 80px;
  color: #1a73e8;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  filter: drop-shadow(0 0 10px rgba(26, 115, 232, 0.3));
}

.login-icon:hover .icon {
  transform: scale(1.1) rotate(10deg);
  filter: drop-shadow(0 0 20px rgba(26, 115, 232, 0.5));
}

.login-form {
  text-align: center;
  position: relative;
  z-index: 1;
}

.form-header {
  margin-bottom: 40px;
}

.login-form h2 {
  font-size: 32px;
  font-weight: 700;
  color: #1a73e8;
  margin: 0 0 12px;
  text-shadow: 0 0 20px rgba(26, 115, 232, 0.3);
  letter-spacing: 1px;
  background: linear-gradient(45deg, #1a73e8, #4285f4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  font-size: 16px;
  color: rgba(26, 115, 232, 0.7);
  margin: 0;
  letter-spacing: 0.5px;
}

.form-body {
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.input-wrapper {
  position: relative;
}

.input-wrapper input {
  width: 100%;
  padding: 14px 18px;
  font-size: 15px;
  color: #fff;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(26, 115, 232, 0.2);
  border-radius: 10px;
  transition: all 0.3s ease;
}

.input-wrapper input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(26, 115, 232, 0.4);
  box-shadow: 0 0 20px rgba(26, 115, 232, 0.1);
}

.input-wrapper input::placeholder {
  color: rgba(26, 115, 232, 0.4);
}

.input-border {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #1a73e8, #4285f4);
  transition: width 0.3s ease;
}

.input-wrapper input:focus + .input-border {
  width: 100%;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(45deg, #1a73e8, #4285f4);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  margin-top: 8px;
  letter-spacing: 1px;
}

.btn-text {
  position: relative;
  z-index: 1;
}

.btn-glow {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.4),
    transparent
  );
  transition: 0.5s;
}

.submit-btn:hover .btn-glow {
  left: 100%;
}

.submit-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(26, 115, 232, 0.3);
}

.submit-btn:active {
  transform: translateY(0);
}

.footer {
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid rgba(26, 115, 232, 0.2);
}

.footer span {
  font-size: 14px;
  color: rgba(26, 115, 232, 0.5);
  letter-spacing: 0.5px;
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
    width: 90%;
    margin: 0 20px;
  }
  
  .blog-intro {
    padding: 30px 20px;
  }
  
  .login-box {
    padding: 30px 20px;
  }
  
  .blog-intro h1 {
    font-size: 28px;
  }
  
  .feature-item {
    padding: 14px;
  }
  
  .gradient-sphere {
    filter: blur(60px);
  }
}
</style>