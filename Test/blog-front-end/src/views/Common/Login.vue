<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="max-w-md w-full">
      <!-- 登录卡片 -->
      <div class="bg-white rounded-2xl shadow-xl overflow-hidden transform transition-all duration-300 hover:shadow-2xl">
        <!-- 顶部装饰条 -->
        <div class="h-2 bg-gradient-to-r from-blue-500 to-indigo-600"></div>
        
        <div class="p-8">
          <!-- Logo和标题 -->
          <div class="text-center mb-8">
            <div class="inline-block p-3 rounded-full bg-blue-50 mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <h2 class="text-3xl font-bold text-gray-800 mb-2">欢迎回来</h2>
            <p class="text-gray-500">请登录您的账号</p>
          </div>

          <!-- 登录表单 -->
          <form @submit.prevent="login" class="space-y-6">
            <!-- 用户名输入框 -->
            <div class="space-y-2">
              <label class="text-sm font-medium text-gray-700">用户名</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
                <input 
                  v-model="username" 
                  type="text" 
                  class="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-300"
                  placeholder="请输入用户名"
                />
              </div>
            </div>

            <!-- 密码输入框 -->
            <div class="space-y-2">
              <label class="text-sm font-medium text-gray-700">密码</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                </div>
                <input 
                  v-model="password" 
                  type="password" 
                  class="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-300"
                  placeholder="请输入密码"
                />
              </div>
            </div>

            <!-- 登录按钮 -->
            <button 
              type="submit" 
              class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-2 rounded-xl hover:from-blue-600 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transform transition-all duration-300 hover:scale-[1.02]"
            >
              登录
            </button>
          </form>

          <!-- 底部链接 -->
          <div class="mt-6 text-center">
            <a href="#" class="text-sm text-blue-500 hover:text-blue-600 transition-colors duration-300">忘记密码？</a>
            <span class="mx-2 text-gray-300">|</span>
            <a href="#" class="text-sm text-blue-500 hover:text-blue-600 transition-colors duration-300">注册账号</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.bg-gradient-to-br {
  background-image: linear-gradient(to bottom right, var(--tw-gradient-stops));
}

.from-blue-50 {
  --tw-gradient-from: #eff6ff;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to, rgba(239, 246, 255, 0));
}

.to-indigo-100 {
  --tw-gradient-to: #e0e7ff;
}

.from-blue-500 {
  --tw-gradient-from: #3b82f6;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to, rgba(59, 130, 246, 0));
}

.to-indigo-600 {
  --tw-gradient-to: #4f46e5;
}

.hover\:from-blue-600:hover {
  --tw-gradient-from: #2563eb;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to, rgba(37, 99, 235, 0));
}

.hover\:to-indigo-700:hover {
  --tw-gradient-to: #4338ca;
}
</style> 