<template>
  <contextHolder/>
  <div class="max-w-md mx-auto p-6 bg-white rounded-lg shadow-md">
    <h2 class="text-2xl font-bold mb-6">添加用户</h2>
    <form @submit.prevent="submitForm">
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="username">
          用户名
        </label>
        <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="username"
            type="text"
            v-model="form.username"
            required
        />
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="password">
          密码
        </label>
        <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="password"
            type="password"
            v-model="form.password"
            required
        />
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
          邮箱
        </label>
        <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="email"
            type="email"
            v-model="form.email"
            required
        />
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="avatar">
          头像
        </label>
        <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="avatar"
            type="file"
            accept="image/*"
            @change="handleFileChange"
        />
        <img v-if="localImageUrl" :src="localImageUrl" class="mt-2 w-20 h-20 object-cover rounded-full" alt="头像"/>
<!--        <div v-if="form.avatarUrl" class="mt-2">-->
<!--          <label class="block text-gray-700 text-sm font-bold mb-2">-->
<!--            HTTPS 地址-->
<!--          </label>-->
<!--          <input-->
<!--              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"-->
<!--              type="text"-->
<!--              v-model="form.avatarUrl"-->
<!--              readonly-->
<!--          />-->
<!--        </div>-->
      </div>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="role">
          身份
        </label>
        <select
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="role"
            v-model="form.role"
            required
        >
          <option value="管理员">管理员</option>
          <option value="用户">用户</option>
          <option value="游客">游客</option>
        </select>
      </div>
      <div class="flex items-center justify-between">
        <button
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            type="submit"
        >
          添加用户
        </button>
      </div>
    </form>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import {post} from "@/net/index.js";
import {message} from "ant-design-vue";
const [messageApi, contextHolder] = message.useMessage();


const form = ref({
  username: '',
  password: '',
  email: '',
  avatarUrl: '',
  role: '用户',
});

const localImageUrl = ref('');
const file = ref(null);

const handleFileChange = (event) => {
  file.value = event.target.files[0];
  if (file.value) {
    const reader = new FileReader();
    reader.onload = (e) => {
      localImageUrl.value = e.target.result;
    };
    reader.readAsDataURL(file.value);
  }
};

//上传头像
const uploadImage = () => {
  return new Promise((resolve, reject) => {
    if (!file.value) {
      resolve('');
      return;
    }

    const img = new Image();
    img.src = localImageUrl.value;
    img.onload = () => {
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      const maxSize = 100; // 裁剪后的最大尺寸
      let width = img.width;
      let height = img.height;

      if (width > height) {
        if (width > maxSize) {
          height *= maxSize / width;
          width = maxSize;
        }
      } else {
        if (height > maxSize) {
          width *= maxSize / height;
          height = maxSize;
        }
      }

      canvas.width = width;
      canvas.height = height;
      ctx.drawImage(img, 0, 0, width, height);
      canvas.toBlob((blob) => {
        const formData = new FormData();
        formData.append('file', blob, file.value.name);
        formData.append('upload_preset', 'fz-image'); // 替换为你的上传预设名称

        fetch('https://api.cloudinary.com/v1_1/dctxdyjk0/image/upload', {//这里要写云名
          method: 'POST',
          body: formData,
        })
            .then((response) => response.json())
            .then((data) => {
              resolve(data.secure_url);
            })
            .catch((error) => {
              reject(error);
            });
      }, 'image/jpeg');
    };
  });
};

const submitForm = async () => {
  try {
    form.value.avatarUrl = await uploadImage();
    post('api/user/addNewUser',{
      username: form.value.username,
      password: form.value.password,
      mail: form.value.email,
      avator: form.value.avatarUrl,
      role: form.value.role,
    },(message)=>{
      messageApi.success(message);
    })
    // 这里可以添加提交表单的逻辑
  } catch (error) {
    console.error('Error uploading image:', error);
  }
};
</script>