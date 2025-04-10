<script setup>
import { useThemeStore } from '@/stores/theme'
import { onMounted } from 'vue'

const themeStore = useThemeStore()

onMounted(() => {
  themeStore.initTheme()
  // 监听系统偏好变化
  window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', e => {
        if (themeStore.isSystem) {
          themeStore.isDark = e.matches
          themeStore.applyTheme()
        }
      })
})
</script>

<template>
  <RouterView class="opacity-50" />
  <div class="fixed bg-opacity-0 bottom-0 left-0 right-0 rounded-xl">
    <div class="container mx-auto flex justify-center items-center mt-2">
      <p class="text-sm">
        <a href="https://beian.miit.gov.cn" class="font-bold font-kai text-red-400 hover:underline p-4" target="_blank">皖ICP备2024066997号</a>
      </p>
    </div>
  </div>
</template>