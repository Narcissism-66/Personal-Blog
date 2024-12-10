import { createRouter, createWebHistory } from 'vue-router'
import{userUserStore} from "@/stores/userStore.js";
import {get} from "@/net/index.js";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/register',
      name: 'register',
      meta:{
        title:'验证页面',
      },
      component: () => import( '../components/Auth/RegistrationForm.vue')
    },
    {
      path: '/auth',
      name: 'login',
      meta:{
        title:'登陆页面',
      },
      component: () => import( '../components/Auth/LoginForm.vue')
    },
    {
      path: '/',
      name: 'Home',
      meta:{
        title:'页面',
      },
      component: () => import( '../views/FrameWork.vue'),
      children:[
        {
          name:"Edit",
          path:"/edit",
          meta:{
            title:'发布页面',
          },
          component:()=>import('../views/Common/EditBlog.vue')
        },
        {
          name:"View",
          path:"/view/:id",
          meta:{
            title:'浏览页面',
          },
          component:()=>import('../views/Common/ViewBlog.vue')
        },
        {
          name:"Index",
          path:"/",
          meta:{
            title:'首页',
          },
          component:()=>import('../views/Common/IndexBlog.vue')
        },
        {
          name:"通知界面",
          path:"/notification",
          meta:{
            title:'通知界面',
          },
          component:()=>import('../views/Common/Notification.vue')
        },
        {
          name:"个人空间",
          path:"/personal",
          meta:{
            title:'个人空间',
          },
          component:()=>import('../views/Common/PersonalSpace.vue')
        },
        {
          name:"粒子特效",
          path:"/particle",
          meta:{
            title:'粒子特效',
          },
          component:()=>import('@/components/Particle-effect.vue')
        },
        {
          name:"管理界面",
          path:"/admin",
          meta: {
            title:"管理界面"
          },
          component:()=>import('../views/Common/Administrator-interface.vue'),
        },
        {
          name:"分类界面",
          path:"/Classification",
          meta:{
            title:'分类界面',
          },
          component:()=>import('../views/Common/Classification-interface.vue')
        }
        ,{
          name:"记笔记",
          path:"/note",
          meta:{
            title:'记笔记',
          },
          component:()=>import('../views/Common/Take-notes.vue')
        },{
          name:"计划",
          path:"/plan",
          meta:{
            title:'计划',
          },
          component:()=>import('../views/Common/PlanList.vue')
        }
      ]
    }
  ]
})

//修改网页名
router.beforeEach((to, from, next)=>
{
  const userStore = userUserStore()
  const publicMap = new Map()
  publicMap.set('/auth', 2)
  publicMap.set('/test', 3)
  publicMap.set('/error/401', 4)
  publicMap.set('/error/404', 5)
  // 检查要访问的路径是否是根路径
  if (to.matched.length === 0) next('error/404')
  if (to.meta.title === undefined) {
    document.title = 'MetaMind'
  } else {
    document.title = to.meta.title
  }
  if (!publicMap.has(to.path)) {
    // 不是访问根路径，检查用户状态
    const user = userStore.user; // 假设你的用户状态保存在Vuex的`user`状态中
    if (user === null) {
      get('api/user/information', {},
          (message, data) => {
            userStore.login(data);
            next();
          }, (message, data) => {
            next('/auth')
          }, (message, data) => {
            next('/auth')
          }
      )
    } else {
      // 用户已登录，允许路由继续
      next();
    }
  } else {
    // 访问的是根路径或其他公开路径，直接放行
    next();
  }
})

export default router
