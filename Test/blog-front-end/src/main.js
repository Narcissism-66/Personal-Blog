import './assets/css/main.css'


import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import axios from "axios";


axios.defaults.baseURL = 'https://www.fzpersonalweb.xyz'
// axios.defaults.baseURL = 'http://localhost:8080'
const BaseUrl='https://www.fzpersonalweb.xyz/';
// const BaseUrl='http://localhost:8080/';

axios.defaults.withCredentials=true;
//后端基础url 之后在请求时只用填写路径 Axios会自动以该url为基础添加路径


const app = createApp(App)
app.config.globalProperties.$axios = axios
app.use(createPinia())
app.use(router).use(ElementPlus).use(Antd);

// 强制启用暗色模式
document.documentElement.classList.add('dark')

app.mount('#app')

export {BaseUrl}
