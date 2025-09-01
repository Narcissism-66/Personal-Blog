/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'selector',
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        'kai': ['KaiTi', '楷体', 'serif'],
      },
      colors: {
        // 深黑色主题颜色
        'dark': {
          'primary': '#000000',      // 纯黑色
          'secondary': '#111111',    // 深黑色
          'tertiary': '#222222',     // 深灰色
          'quaternary': '#333333',   // 中深灰色
          'accent': '#444444',       // 强调色
          'border': '#333333',       // 边框色
          'text': {
            'primary': '#ffffff',    // 主要文字
            'secondary': '#cccccc',  // 次要文字
            'muted': '#999999',      // 静音文字
          },
          'surface': {
            'primary': '#000000',    // 主要表面
            'secondary': '#111111',  // 次要表面
            'tertiary': '#222222',   // 第三表面
          }
        }
      },
    },
  },
  plugins: [],
}