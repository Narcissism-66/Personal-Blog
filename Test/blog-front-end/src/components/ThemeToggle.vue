<template>
  <div class="theme-toggle">
    <div class="theme-indicator system-glow">
      <svg class="theme-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"
        />
      </svg>
      <span class="theme-text">纯黑色主题</span>
    </div>
  </div>
</template>

<script setup>
// 不需要任何逻辑，固定显示纯黑色主题
</script>

<style scoped>
.theme-toggle {
  display: inline-block;
}

.theme-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: var(--bg-primary, #000000);
  color: var(--system-accent, #007AFF);
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  border: 1px solid var(--system-accent, #007AFF);
  transition: all 0.3s ease;
}

.theme-indicator:hover {
  background: var(--bg-secondary, #000000);
  border-color: var(--system-accent, #007AFF);
  box-shadow: 0 0 20px var(--shadow-medium, rgba(0, 122, 255, 0.2));
  transform: translateY(-1px);
}

.theme-icon {
  width: 18px;
  height: 18px;
  color: var(--system-accent, #007AFF);
}

.theme-text {
  color: var(--system-accent, #007AFF);
}

/* 深黑色主题下的样式 */
.dark .theme-indicator {
  background: var(--dark-bg-primary, #000000);
  color: var(--dark-system-accent, #007AFF);
  border-color: var(--dark-system-accent, #007AFF);
}

.dark .theme-indicator:hover {
  background: var(--dark-bg-secondary, #000000);
  border-color: var(--dark-system-accent, #007AFF);
  box-shadow: 0 0 20px var(--dark-shadow, rgba(0, 122, 255, 0.1));
}

.dark .theme-icon {
  color: var(--dark-system-accent, #007AFF);
}

.dark .theme-text {
  color: var(--dark-system-accent, #007AFF);
}
</style> 