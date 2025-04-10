<script setup>
import {onMounted, reactive, ref} from "vue";
import {get, post} from "@/net/index.js";
import {message} from "ant-design-vue";
import {userUserStore} from "@/stores/userStore.js";
import {parseDateTime} from "../../util/DateFormattor.js";
const [messageApi, contextHolder] = message.useMessage();
const userStore=userUserStore();

const options=reactive({
  content:"",
  plan:[],
})

const addNewPlan=()=>{
  post('api/plan/addNewPlan',{
    plan:options.content,
  },(message,data)=>{
    messageApi.success(message);
    getAllPlansByUserId();
  })
}
const IsMakePlan=ref(false);
const MakePlan=()=>{
  IsMakePlan.value=!IsMakePlan.value;
  console.log(IsMakePlan.value);
}

const getAllPlansByUserId=()=>{
  get("api/plan/getAllPlansByUserId",{},(message,data)=>{
    options.plan=data;
  })
}
const UpdateFinish=(finish,id)=>{
  post('api/plan/UpdateFinish',{
    finish:finish,
    id:id
  },(message,data)=>{
    getAllPlansByUserId();
  })
}

const DeletePlan=(id)=>{
  post('api/plan/deletePlan',{
    id:id,
  },(message,data)=>{
    messageApi.success(message);
    getAllPlansByUserId();
  })
}
onMounted(()=>{
  getAllPlansByUserId();
})
</script>

<template>
  <contextHolder/>
  <div class="  mt-22 overflow-hidden flex flex-nowrap">
    <div>
      <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 64 64" class="hover:scale-110  active:scale-90 size-12 absolute right-10  mt-24" @click="MakePlan">
        <path fill="currentColor" d="M62.2 11.9c0-.8-.3-1.6-.9-2.2c-1.2-1.2-2.4-2.4-3.5-3.6c-1.1-1.1-2.1-2.2-3.2-3.2c-.5-.6-1.1-1-1.9-1.1c-.9-.1-1.7.1-2.4.7l-6.8 6.8H8.1c-3.4 0-6.3 2.8-6.3 6.3V56c0 3.4 2.8 6.3 6.3 6.3h40.5c3.4 0 6.3-2.8 6.3-6.3V20.5l6.5-6.5c.5-.6.8-1.3.8-2.1M32.8 36c-.1.1-.1.1-.2.1l-7.2 2.4l2.4-7.2c0-.1.1-.1.1-.2l18-18l5 4.9zm17.5 20c0 1-.8 1.8-1.8 1.8H8.1c-1 0-1.8-.8-1.8-1.8V15.5c0-1 .8-1.8 1.8-1.8h30.8L24.7 28c-.5.5-1 1.2-1.2 2l-3.7 11.2c-.3.8-.1 1.5.3 2.2c.3.4.9 1 2 1h.4L34 40.6c.7-.2 1.4-.7 1.9-1.2L50.3 25zM54 14.9L49 10l3.1-3.1c.8.8 4.1 4.1 4.9 5z"/>
      </svg>
    </div>

    <div v-if="IsMakePlan === true" class="mt-24 absolute right-10 top-20 flex flex-wrap gap-4">
      <div class="bg-white w-full h-full bg-opacity-50 rounded-xl p-4">
        <input
            v-model="options.content"
            class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 font-kai"
            placeholder="添加新的计划"
        />
        <div class="flex justify-center mt-4">
          <button @click="addNewPlan" class="px-4 py-1  text-xl font-kai text-yellow-600 bg-white border border-yellow-600 rounded-lg hover:bg-blue-400 hover:text-white">
            添加
          </button>
        </div>
      </div>
    </div>

<!--    展示计划-->
    <div class="w-3/5 mx-auto mt-20 p-6 bg-white rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold mb-4">计划清单</h1>
      <ul class="scrollbar-hide overflow-y-auto " style="max-height: 480px; ">
        <li @click="UpdateFinish(plan.finish === 'false' ? 'true' : 'false', plan.id)" v-for="plan in options.plan" class="group flex items-center p-2 border-b border-gray-200 cursor-pointer relative " >
          <span :class="plan.finish === 'true' ? 'text-green-500' : 'text-gray-300'" class="mr-3">
            {{ plan.finish === 'true' ? '☑' : '☐' }}
          </span>
          <span :class="plan.finish === 'true' ? 'line-through text-gray-500' : 'text-gray-800'" class="w-1/2 font-kai  text-xl">
            {{ plan.plan }}
          </span>
          <span class="font-kai text-blue-300  ">
            制定时间：{{parseDateTime(plan.time)}}
          </span>
          <div v-if="plan.authorId === userStore.user.id" class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-1000">
            <button @click.stop="DeletePlan(plan.id)" class="bg-red-500 text-white px-2 py-1 rounded-md font-kai">删除</button>
          </div>
        </li>
      </ul>
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