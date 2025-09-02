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

// 强制启用深黑色主题
document.documentElement.classList.add('dark')

// 设置纯黑色主题的CSS变量
document.documentElement.style.setProperty('--dark-bg-primary', '#000000')
document.documentElement.style.setProperty('--dark-bg-secondary', '#000000')
document.documentElement.style.setProperty('--dark-bg-tertiary', '#000000')
document.documentElement.style.setProperty('--dark-bg-quaternary', '#000000')
document.documentElement.style.setProperty('--dark-bg-accent', '#000000')

// 设置系统颜色特效变量
document.documentElement.style.setProperty('--dark-system-accent', '#007AFF')        // iOS 蓝色
document.documentElement.style.setProperty('--dark-system-success', '#34C759')      // iOS 绿色
document.documentElement.style.setProperty('--dark-system-warning', '#FF9500')      // iOS 橙色
document.documentElement.style.setProperty('--dark-system-error', '#FF3B30')        // iOS 红色
document.documentElement.style.setProperty('--dark-system-purple', '#AF52DE')       // iOS 紫色
document.documentElement.style.setProperty('--dark-system-pink', '#FF2D92')         // iOS 粉色
document.documentElement.style.setProperty('--dark-system-teal', '#5AC8FA')        // iOS 青色
document.documentElement.style.setProperty('--dark-system-indigo', '#5856D6')      // iOS 靛蓝

// 设置文字和边框颜色
document.documentElement.style.setProperty('--dark-text-primary', '#ffffff')
document.documentElement.style.setProperty('--dark-text-secondary', '#8E8E93')     // iOS 次要文字
document.documentElement.style.setProperty('--dark-text-muted', '#48484A')         // iOS 静音文字
document.documentElement.style.setProperty('--dark-border', '#38383A')             // iOS 分割线
document.documentElement.style.setProperty('--dark-border-light', '#48484A')       // iOS 轻分割线

// 设置阴影颜色
document.documentElement.style.setProperty('--dark-shadow', 'rgba(0, 122, 255, 0.1)')
document.documentElement.style.setProperty('--dark-shadow-light', 'rgba(0, 122, 255, 0.05)')

// 设置组件相关的CSS变量
document.documentElement.style.setProperty('--bg-primary', '#000000')
document.documentElement.style.setProperty('--bg-secondary', '#000000')
document.documentElement.style.setProperty('--bg-tertiary', '#000000')
document.documentElement.style.setProperty('--bg-quaternary', '#000000')
document.documentElement.style.setProperty('--text-primary', '#ffffff')
document.documentElement.style.setProperty('--text-secondary', '#8E8E93')
document.documentElement.style.setProperty('--text-muted', '#48484A')
document.documentElement.style.setProperty('--border-color', '#38383A')
document.documentElement.style.setProperty('--accent-color', '#007AFF')
document.documentElement.style.setProperty('--shadow-light', 'rgba(0, 122, 255, 0.1)')
document.documentElement.style.setProperty('--shadow-medium', 'rgba(0, 122, 255, 0.2)')
document.documentElement.style.setProperty('--shadow-heavy', 'rgba(0, 122, 255, 0.3)')

// 设置系统颜色变量
document.documentElement.style.setProperty('--system-accent', '#007AFF')
document.documentElement.style.setProperty('--system-success', '#34C759')
document.documentElement.style.setProperty('--system-warning', '#FF9500')
document.documentElement.style.setProperty('--system-error', '#FF3B30')
document.documentElement.style.setProperty('--system-purple', '#AF52DE')
document.documentElement.style.setProperty('--system-pink', '#FF2D92')
document.documentElement.style.setProperty('--system-teal', '#5AC8FA')
document.documentElement.style.setProperty('--system-indigo', '#5856D6')

// 确保所有组件都使用深黑色主题
document.documentElement.style.setProperty('color-scheme', 'dark')

app.mount('#app')

export {BaseUrl}
