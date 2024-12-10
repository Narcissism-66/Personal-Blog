<script setup>
import { useWebSocket, sendMessage, closeWebSocket } from '@/server/websocket.js';
import { UserOnline } from '@/stores/onlineStore';
import { onMounted, onUnmounted, ref, watch } from 'vue';
import { post } from "@/net/index.js";
import { userUserStore } from "@/stores/userStore.js";
import { startSakura } from "@/util/FullScreenFlower.js";

const onlineStore = UserOnline();
const userStore = userUserStore();
const isOnline = ref(false);

const UpdateOnline = () => {
  post('api/user/UpdateOnline', {
    userId: userStore.user.id,
    isOnline: isOnline.value
  }, (message) => {
    if (isOnline.value === true) {
      onlineStore.AddOnline();
    } else {
      onlineStore.DecreaseOnline();
    }
    console.log(onlineStore.getOnline);

    // 发送 onlineStore.online 的值给所有用户
    sendMessage({
      type: 'onlineUpdate',
      onlineCount: onlineStore.getOnline
    });
    console.log('Online status updated successfully');
  });
};

const handleVisibilityChange = () => {
  if (userStore.user) {
    isOnline.value = document.visibilityState === 'visible';
    console.log(`User is ${isOnline.value ? 'online' : 'offline'}`);
    UpdateOnline(); // 更新在线状态
  }
};

const handleBeforeUnload = () => {
  if (userStore.user) {
    isOnline.value = false; // 设置为离线状态
    UpdateOnline(); // 更新在线状态
    document.removeEventListener('visibilitychange', handleVisibilityChange);
  }
};

onMounted(async () => {
  // 初始化 WebSocket 连接
  const { socket } = useWebSocket('ws://localhost:5173/');
  if (userStore.user) {
    document.addEventListener('visibilitychange', handleVisibilityChange);
    window.addEventListener('beforeunload', handleBeforeUnload);
    handleVisibilityChange(); // 初始化在线状态

    socket.value.onopen = () => {
      console.log("WebSocket connection opened.");

      socket.value.onmessage = (event) => {
        console.log("收到了吗")
        try {
          const data = JSON.parse(event.data);
          if (data.type === 'onlineUpdate') {
            onlineStore.SetOnline(data.onlineCount);
            console.log("getOnline" + onlineStore.getOnline);
            console.log(`Online users: ${data.onlineCount}`);
          }
        } catch (error) {
          console.error("Error parsing message:", error);
        }
      };
   };
  }
});

onUnmounted(() => {
  if (userStore.user) {
    document.removeEventListener('visibilitychange', handleVisibilityChange);
    window.removeEventListener('beforeunload', handleBeforeUnload);
    closeWebSocket(); // 关闭 WebSocket 连接
  }
});

watch(() => userStore.user, (newUser, oldUser) => {
  if (newUser && !oldUser) {
    document.addEventListener('visibilitychange', handleVisibilityChange);
    window.addEventListener('beforeunload', handleBeforeUnload);
    handleVisibilityChange(); // 初始化在线状态
  } else if (!newUser && oldUser) {
    document.removeEventListener('visibilitychange', handleVisibilityChange);
    window.removeEventListener('beforeunload', handleBeforeUnload);
    closeWebSocket(); // 关闭 WebSocket 连接
  }
}, { deep: true });
</script>

<template>
  <RouterView :class="startSakura" class="opacity-50" />
  <div class="fixed bottom-0 left-0 right-0 rounded-xl">
    <div class="container mx-auto flex justify-center items-center mt-2">
      <p class="text-sm">
        <a href="https://beian.miit.gov.cn" class="font-bold font-kai text-red-400 hover:underline p-4" target="_blank">皖ICP备2024066997号</a>
      </p>
    </div>
  </div>
</template>