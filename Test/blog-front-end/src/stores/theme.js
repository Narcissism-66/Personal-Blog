import { defineStore } from 'pinia'

export const useThemeStore = defineStore('theme', {
  state: () => ({
    isDark: true,  // 固定为深黑色主题
    isSystem: false  // 不跟随系统
  }),

  actions: {
    // 初始化主题（固定启用深黑色主题）
    initTheme() {
      // 强制使用深黑色主题
      this.isDark = true
      this.isSystem = false
      this.applyTheme()
    },

    // 应用主题到DOM
    applyTheme() {
      const root = document.documentElement
      // 强制添加dark类
      root.classList.add('dark')
      root.classList.remove('light')

      // 平滑过渡处理
      root.classList.add('theme-transition')
      setTimeout(() => {
        root.classList.remove('theme-transition')
      }, 300)
    }
  },

  getters: {
    currentTheme: (state) => 'dark'  // 固定返回dark
  }
})