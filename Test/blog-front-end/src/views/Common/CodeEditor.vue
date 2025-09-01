<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { message } from 'ant-design-vue'
import { EditorView, basicSetup } from 'codemirror'
import { EditorState } from '@codemirror/state'
import { cpp } from '@codemirror/lang-cpp'
import { java } from '@codemirror/lang-java'
import { oneDark } from '@codemirror/theme-one-dark'
import { post } from '@/net/index.js'

const [messageApi, contextHolder] = message.useMessage()

// 响应式数据
const currentLanguage = ref('cpp')
const cursorLine = ref(1)
const cursorColumn = ref(1)
const codeLength = ref(0)
const editorContainer = ref(null)
const isRunning = ref(false)
const runOutput = ref('')
const runError = ref('')
const userInput = ref('')

// 三栏布局宽度控制
const filePanelWidth = ref(280)
const editorPanelWidth = ref(800)
const outputPanelWidth = ref(400)
const isResizing = ref(false)
const resizeType = ref('')
const startX = ref(0)
const startWidths = ref({ file: 0, editor: 0, output: 0 })
let editor = null

// 文件管理
const files = reactive([
  {
    id: 1,
    name: 'main.cpp',
    content: `#include <bits/stdc++.h>
using namespace std;

int main() {
    cout << "Hello, World!" << endl;
    return 0;
}`,
    language: 'cpp'
  },
  {
    id: 2,
    name: 'main.c',
    content: `#include <stdio.h>

int main() {
    printf("Hello, World!\\n");
    return 0;
}`,
    language: 'c'
  },
  {
    id: 3,
    name: 'Main.java',
    content: `public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}`,
    language: 'java'
  }
])

const currentFile = ref(files[0])
const openFiles = ref([files[0]])

// 计算属性
const getLanguageSupport = (language) => {
  switch (language) {
    case 'c':
    case 'cpp':
      return cpp()
    case 'java':
      return java()
    default:
      return cpp()
  }
}

// 方法
const initEditor = () => {
  if (!editorContainer.value) return

  const state = EditorState.create({
    doc: currentFile.value.content,
    extensions: [
      basicSetup,
      getLanguageSupport(currentLanguage.value),
      oneDark,
      EditorView.updateListener.of((update) => {
        if (update.docChanged) {
          currentFile.value.content = update.state.doc.toString()
          codeLength.value = update.state.doc.length
        }
        if (update.selectionSet) {
          const pos = update.state.selection.main.head
          const line = update.state.doc.lineAt(pos)
          cursorLine.value = line.number
          cursorColumn.value = pos - line.from + 1
        }
      })
    ]
  })

  editor = new EditorView({
    state,
    parent: editorContainer.value
  })
}

const updateLanguage = () => {
  if (currentFile.value) {
    currentFile.value.language = currentLanguage.value
    if (editor) {
      const newState = editor.state.update({
        effects: EditorState.reconfigure.of([
          basicSetup,
          getLanguageSupport(currentLanguage.value),
          oneDark
        ])
      })
      editor.dispatch(newState)
    }
  }
}

const newFile = () => {
  const extensions = {
    c: 'c',
    cpp: 'cpp',
    java: 'java'
  }

  const newFileObj = {
    id: Date.now(),
    name: `main.${extensions[currentLanguage.value]}`,
    content: getDefaultContent(currentLanguage.value),
    language: currentLanguage.value
  }

  files.push(newFileObj)
  selectFile(newFileObj)
  messageApi.success('新建文件成功')
}

const getDefaultContent = (language) => {
  switch (language) {
    case 'c':
      return `#include <stdio.h>

int main() {
    printf("Hello, World!\\n");
    return 0;
}`
    case 'cpp':
      return `#include <bits/stdc++.h>
using namespace std;

int main() {
    cout << "Hello, World!" << endl;
    return 0;
}`
    case 'java':
      return `public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}`
    default:
      return ''
  }
}

const runCode = async () => {
  if (!currentFile.value || !editor) return

  const code = editor.state.doc.toString()
  if (!code.trim()) {
    messageApi.warning('请先输入代码')
    return
  }

  isRunning.value = true
  runOutput.value = ''
  runError.value = ''

  try {
    post('/api/code/runCode', {
      code: code,
      input: userInput.value,
      language: currentFile.value.language // 使用当前文件的语言
    }, (message, data) => {
      // 添加一个小延迟，让用户能看到加载完成的状态
      setTimeout(() => {
        runOutput.value = data || '运行成功，无输出';
        isRunning.value = false;
        messageApi.success('代码运行成功');
      }, 300);
    }, (message) => {
      // 添加一个小延迟，让用户能看到加载完成的状态
      setTimeout(() => {
        runError.value = '运行错误: ' + message;
        isRunning.value = false;
        messageApi.error('代码运行失败');
      }, 300);
    });
  } catch (error) {
    runError.value = error.message
    messageApi.error('请求发送失败: ' + error.message)
    isRunning.value = false
  }
}

const downloadFile = () => {
  if (!currentFile.value || !editor) return

  const content = editor.state.doc.toString()
  const blob = new Blob([content], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = currentFile.value.name
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
  messageApi.success('文件下载成功')
}

const selectFile = (file) => {
  currentFile.value = file
  currentLanguage.value = file.language

  if (!openFiles.value.find(f => f.id === file.id)) {
    openFiles.value.push(file)
  }

  if (editor) {
    const newState = editor.state.update({
      changes: {
        from: 0,
        to: editor.state.doc.length,
        insert: file.content
      }
    })
    editor.dispatch(newState)
  }
}

const closeFile = (file) => {
  const index = openFiles.value.findIndex(f => f.id === file.id)
  if (index > -1) {
    openFiles.value.splice(index, 1)
    if (currentFile.value?.id === file.id) {
      currentFile.value = openFiles.value[openFiles.value.length - 1] || null
      if (currentFile.value) {
        selectFile(currentFile.value)
      }
    }
  }
}

// 宽度调整相关方法
const startResize = (type, event) => {
  isResizing.value = true
  resizeType.value = type
  startX.value = event.clientX
  startWidths.value = {
    file: filePanelWidth.value,
    editor: editorPanelWidth.value,
    output: outputPanelWidth.value
  }

  document.body.style.cursor = 'col-resize'
  document.body.style.userSelect = 'none'

  // 添加拖拽时的样式
  document.body.classList.add('resizing')
}

const handleResize = (event) => {
  if (!isResizing.value) return

  const deltaX = event.clientX - startX.value
  const containerWidth = document.querySelector('.flex-1.flex.overflow-hidden.relative.z-10.w-full.min-w-0').offsetWidth

  if (resizeType.value === 'file') {
    // 调整文件面板宽度，同时调整编辑器宽度
    const newFileWidth = Math.max(200, Math.min(500, startWidths.value.file + deltaX))
    const newEditorWidth = containerWidth - newFileWidth - startWidths.value.output

    if (newEditorWidth >= 400) {
      filePanelWidth.value = newFileWidth
      editorPanelWidth.value = newEditorWidth
    }
  } else if (resizeType.value === 'editor') {
    // 调整编辑器宽度，同时调整输出面板宽度
    const newEditorWidth = Math.max(400, Math.min(1200, startWidths.value.editor + deltaX))
    const newOutputWidth = containerWidth - startWidths.value.file - newEditorWidth

    if (newOutputWidth >= 250) {
      editorPanelWidth.value = newEditorWidth
      outputPanelWidth.value = newOutputWidth
    }
  } else if (resizeType.value === 'output') {
    // 调整输出面板宽度，同时调整编辑器宽度
    const newOutputWidth = Math.max(250, Math.min(600, startWidths.value.output - deltaX))
    const newEditorWidth = containerWidth - startWidths.value.file - newOutputWidth

    if (newEditorWidth >= 400) {
      outputPanelWidth.value = newOutputWidth
      editorPanelWidth.value = newEditorWidth
    }
  }

  // 确保总宽度等于容器宽度
  const totalWidth = filePanelWidth.value + editorPanelWidth.value + outputPanelWidth.value
  if (Math.abs(totalWidth - containerWidth) > 1) {
    // 如果总宽度不匹配，调整编辑器宽度来补偿
    editorPanelWidth.value = containerWidth - filePanelWidth.value - outputPanelWidth.value
  }
}

const stopResize = () => {
  isResizing.value = false
  resizeType.value = ''
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
  document.body.classList.remove('resizing')

  // 确保面板宽度是整数
  filePanelWidth.value = Math.round(filePanelWidth.value)
  editorPanelWidth.value = Math.round(editorPanelWidth.value)
  outputPanelWidth.value = Math.round(outputPanelWidth.value)
}

// 添加窗口大小变化监听
const handleWindowResize = () => {
  if (!isResizing.value) {
    const containerWidth = document.querySelector('.flex-1.flex.overflow-hidden.relative.z-10.w-full.min-w-0')?.offsetWidth || 0
    if (containerWidth > 0) {
      // 按比例重新分配宽度
      const totalWidth = filePanelWidth.value + editorPanelWidth.value + outputPanelWidth.value
      const ratio = containerWidth / totalWidth

      filePanelWidth.value = Math.round(filePanelWidth.value * ratio)
      editorPanelWidth.value = Math.round(editorPanelWidth.value * ratio)
      outputPanelWidth.value = Math.round(outputPanelWidth.value * ratio)

      // 确保最小宽度限制
      if (filePanelWidth.value < 200) filePanelWidth.value = 200
      if (editorPanelWidth.value < 400) editorPanelWidth.value = 400
      if (outputPanelWidth.value < 250) outputPanelWidth.value = 250

      // 重新计算编辑器宽度以填满剩余空间
      editorPanelWidth.value = containerWidth - filePanelWidth.value - outputPanelWidth.value
    }
  }
}

const clearOutput = () => {
  runOutput.value = ''
  runError.value = ''
  userInput.value = ''
}

// 生命周期
onMounted(() => {
  nextTick(() => {
    initEditor()
    if (currentFile.value) {
      codeLength.value = currentFile.value.content.length
    }

    // 初始化时确保面板占满容器
    handleWindowResize()

    // 添加窗口大小变化监听
    window.addEventListener('resize', handleWindowResize)
  })
})

onUnmounted(() => {
  if (editor) {
    editor.destroy()
  }
  window.removeEventListener('resize', handleWindowResize)
})
</script>

<template>
  <div class="h-screen flex flex-col bg-black text-white font-sans relative overflow-hidden">
    <!-- 背景装饰 -->
    <div class="absolute inset-0 pointer-events-none z-0">
      <div class="absolute top-0 left-0 w-full h-full bg-gradient-radial from-white/2 via-transparent to-transparent"></div>
      <div class="absolute top-0 right-0 w-full h-full bg-gradient-radial from-white/2 via-transparent to-transparent"></div>
    </div>
    
    <contextHolder/>
    
    <!-- GitHub风格的顶部工具栏 -->
    <div class="flex justify-between items-center px-5 py-3 bg-gray-900 border-b border-gray-700 h-14 backdrop-blur-md relative z-10 shadow-lg">
      <div class="flex items-center flex-1">
        <div class="flex gap-1 bg-gray-800 p-1 rounded-xl border border-gray-700">
          <div 
            v-for="file in openFiles" 
            :key="file.id"
            @click="selectFile(file)"
            :class="[
              'flex items-center gap-2 px-4 py-2 bg-transparent border-none cursor-pointer text-sm font-medium transition-all duration-300 rounded-lg text-gray-300 relative overflow-hidden',
              { 
                'bg-gray-800 text-white shadow-lg -translate-y-0.5 border border-gray-700': currentFile?.id === file.id,
                'hover:text-white hover:-translate-y-0.5': currentFile?.id !== file.id
              }
            ]"
          >
            <svg class="w-4 h-4 transition-all duration-300" viewBox="0 0 16 16" fill="currentColor">
              <path d="M3.75 1.5a.25.25 0 0 0-.25.25v11.5c0 .138.112.25.25.25h8.5a.25.25 0 0 0 .25-.25V6H9.75A1.75 1.75 0 0 1 8 4.25V1.5H3.75zm5.75.56v2.19c0 .138.112.25.25.25h2.19L9.5 2.06zM2 1.75C2 .784 2.784 0 3.75 0h5.086c.464 0 .909.184 1.237.513l3.414 3.414c.329.328.513.773.513 1.237v8.086A1.75 1.75 0 0 1 12.25 15h-8.5A1.75 1.75 0 0 1 2 13.25V1.75z"/>
            </svg>
            <span class="file-name">{{ file.name }}</span>
            <button @click.stop="closeFile(file)" class="bg-white/10 border-none text-gray-300 cursor-pointer p-1 rounded-md flex items-center justify-center transition-all duration-300 ml-2 hover:bg-red-500 hover:text-white hover:scale-110" title="关闭文件">
              <svg class="w-3 h-3" viewBox="0 0 16 16" fill="currentColor">
                <path d="M3.72 3.72a.75.75 0 0 1 1.06 0L8 6.94l3.22-3.22a.749.749 0 0 1 1.275.326.749.749 0 0 1-.215.734L9.06 8l3.22 3.22a.749.749 0 0 1-.326 1.275.749.749 0 0 1-.734-.215L8 9.06l-3.22 3.22a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042L6.94 8 3.72 4.78a.75.75 0 0 1 0-1.06z"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
      
      <div class="flex items-center gap-4">
        <div class="language-selector">
          <select v-model="currentLanguage" @change="updateLanguage" class="px-4 py-2 border border-gray-700 rounded-lg bg-gray-800 text-white text-sm font-medium cursor-pointer transition-all duration-300 backdrop-blur-md hover:border-blue-500 hover:shadow-[0_0_0_3px_rgba(59,130,246,0.1)] focus:outline-none focus:border-blue-500 focus:shadow-[0_0_0_3px_rgba(59,130,246,0.2)]">
            <option value="c">C</option>
            <option value="cpp">C++</option>
            <option value="java">Java</option>
          </select>
        </div>
        
        <div class="flex gap-3">
          <button @click="newFile" class="flex items-center gap-2 px-4 py-2.5 border-none rounded-lg cursor-pointer text-sm font-semibold transition-all duration-300 relative overflow-hidden backdrop-blur-md bg-gray-800 text-white border border-gray-700 hover:bg-gray-700 hover:border-gray-300 hover:-translate-y-0.5 hover:shadow-[0_8px_25px_rgba(0,0,0,0.7)]" title="新建文件">
            <svg class="w-4 h-4 transition-transform duration-300 group-hover:scale-110" viewBox="0 0 16 16" fill="currentColor">
              <path d="M1.75 1A1.75 1.75 0 0 0 0 2.75v10.5C0 14.216.784 15 1.75 15h12.5A1.75 1.75 0 0 0 16 13.25v-8.5A1.75 1.75 0 0 0 14.25 3H7.5a.75.75 0 0 1 0-1.5h6.75a.25.25 0 0 1 .25.25v10.5a.25.25 0 0 1-.25.25H1.75a.25.25 0 0 1-.25-.25V2.75a.25.25 0 0 1 .25-.25H8.25a.75.75 0 0 1 0 1.5H1.75Z"/>
              <path d="M7 5.75C7 5.336 7.336 5 7.75 5h3.5a.75.75 0 0 1 .75.75v3.5a.75.75 0 0 1-.75.75h-3.5A.75.75 0 0 1 7 9.25v-3.5Zm1.5.75v2h2v-2h-2Z"/>
            </svg>
            新建
          </button>
          
          <button @click="runCode" class="flex items-center gap-2 px-4 py-2.5 border-none rounded-lg cursor-pointer text-sm font-semibold transition-all duration-300 relative overflow-hidden backdrop-blur-md bg-gray-800 text-white border border-gray-700 hover:bg-gray-700 hover:border-gray-300 hover:-translate-y-0.5 hover:shadow-[0_8px_25px_rgba(0,0,0,0.7)]" title="运行代码">
            <svg class="w-4 h-4 transition-transform duration-300 group-hover:scale-110" viewBox="0 0 16 16" fill="currentColor">
              <path d="M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0ZM1.5 8a6.5 6.5 0 1 0 13 0 6.5 6.5 0 0 0-13 0Zm4.879-2.773 4.264 2.559a.25.25 0 0 1 0 .428l-4.264 2.559A.25.25 0 0 1 6 10.559V5.442a.25.25 0 0 1 .379-.215Z"/>
            </svg>
            运行
          </button>
          
          <button @click="downloadFile" class="flex items-center gap-2 px-4 py-2.5 border-none rounded-lg cursor-pointer text-sm font-semibold transition-all duration-300 relative overflow-hidden backdrop-blur-md bg-gray-800 text-white border border-gray-700 hover:bg-gray-700 hover:border-gray-300 hover:-translate-y-0.5 hover:shadow-[0_8px_25px_rgba(0,0,0,0.7)]" title="下载文件">
            <svg class="w-4 h-4 transition-transform duration-300 group-hover:scale-110" viewBox="0 0 16 16" fill="currentColor">
              <path d="M3.75 1.5a.25.25 0 0 0-.25.25v11.5c0 .138.112.25.25.25h8.5a.25.25 0 0 0 .25-.25V6H9.75A1.75 1.75 0 0 1 8 4.25V1.5H3.75zm5.75.56v2.19c0 .138.112.25.25.25h2.19L9.5 2.06zM2 1.75C2 .784 2.784 0 3.75 0h5.086c.464 0 .909.184 1.237.513l3.414 3.414c.329.328.513.773.513 1.237v8.086A1.75 1.75 0 0 1 12.25 15h-8.5A1.75 1.75 0 0 1 2 13.25V1.75z"/>
            </svg>
            下载
          </button>
        </div>
      </div>
    </div>

    <!-- 三栏布局主体 -->
    <div class="flex-1 flex overflow-hidden relative z-10 w-full min-w-0" @mousemove="handleResize" @mouseup="stopResize">
      <!-- 左侧文件区域 -->
      <div class="flex flex-col bg-gray-900 border-r border-gray-700 relative backdrop-blur-md min-w-0 flex-shrink-0 overflow-hidden transition-[width] duration-75 ease-out" :style="{ width: filePanelWidth + 'px' }">
        <div class="flex justify-between items-center px-5 py-4 border-b border-gray-700 bg-gray-800 relative">
          <h3 class="m-0 text-sm font-bold text-white flex items-center gap-2">
            <div class="w-1 h-4 bg-gray-300 rounded-sm"></div>
            文件资源管理器
          </h3>
        </div>
        <div class="flex-1 py-3 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-700 scrollbar-track-transparent hover:scrollbar-thumb-gray-300">
          <div 
            v-for="file in files" 
            :key="file.id"
            @click="selectFile(file)"
            :class="[
              'flex items-center gap-3 px-5 py-3 cursor-pointer transition-all duration-300 text-sm font-medium text-gray-300 relative mx-2 rounded-lg',
              { 
                'bg-gray-800 text-white shadow-lg translate-x-1 border border-gray-700': currentFile?.id === file.id,
                'hover:bg-gray-800 hover:text-white hover:translate-x-1': currentFile?.id !== file.id
              }
            ]"
          >
            <div class="absolute left-0 top-1/2 -translate-y-1/2 w-0.5 h-0 bg-gray-300 rounded-r-sm transition-all duration-300" :class="{ 'h-3/5': currentFile?.id !== file.id, 'h-4/5 bg-white': currentFile?.id === file.id }"></div>
            <svg class="w-4 h-4 transition-all duration-300" :class="{ 'text-white scale-110': currentFile?.id === file.id }" viewBox="0 0 16 16" fill="currentColor">
              <path d="M3.75 1.5a.25.25 0 0 0-.25.25v11.5c0 .138.112.25.25.25h8.5a.25.25 0 0 0 .25-.25V6H9.75A1.75 1.75 0 0 1 8 4.25V1.5H3.75zm5.75.56v2.19c0 .138.112.25.25.25h2.19L9.5 2.06zM2 1.75C2 .784 2.784 0 3.75 0h5.086c.464 0 .909.184 1.237.513l3.414 3.414c.329.328.513.773.513 1.237v8.086A1.75 1.75 0 0 1 12.25 15h-8.5A1.75 1.75 0 0 1 2 13.25V1.75z"/>
            </svg>
            <span class="file-name">{{ file.name }}</span>
          </div>
        </div>
        
        <!-- 文件区域调整手柄 -->
        <div 
          class="absolute top-0 bottom-0 w-2 bg-transparent cursor-col-resize z-10 transition-all duration-200 flex items-center justify-center -right-1 hover:bg-white/5"
          @mousedown="(event) => startResize('file', event)"
        >
          <div class="w-0.5 h-10 bg-gray-700 rounded-sm transition-all duration-200 hover:h-15 hover:w-0.75 hover:bg-gray-300"></div>
        </div>
      </div>

      <!-- 中间代码编辑区域 -->
      <div class="flex flex-col bg-gray-900 border-r border-gray-700 relative backdrop-blur-md min-w-0 flex-shrink-0 overflow-hidden transition-[width] duration-75 ease-out" :style="{ width: editorPanelWidth + 'px' }">
        <div class="flex justify-between items-center px-5 py-4 border-b border-gray-700 bg-gray-800 relative">
          <h3 class="m-0 text-sm font-bold text-white flex items-center gap-2">
            <div class="w-1 h-4 bg-gray-300 rounded-sm"></div>
            代码编辑器
          </h3>
        </div>
        <div ref="editorContainer" class="flex-1 overflow-hidden bg-black rounded-b-xl mx-2 mb-2 shadow-[inset_0_2px_8px_rgba(0,0,0,0.5)] w-full min-w-0"></div>
        
        <!-- 编辑器区域调整手柄 -->
        <div 
          class="absolute top-0 bottom-0 w-2 bg-transparent cursor-col-resize z-10 transition-all duration-200 flex items-center justify-center -left-1 hover:bg-white/5"
          @mousedown="(event) => startResize('editor', event)"
        >
          <div class="w-0.5 h-10 bg-gray-700 rounded-sm transition-all duration-200 hover:h-15 hover:w-0.75 hover:bg-gray-300"></div>
        </div>
        <div 
          class="absolute top-0 bottom-0 w-2 bg-transparent cursor-col-resize z-10 transition-all duration-200 flex items-center justify-center -right-1 hover:bg-white/5"
          @mousedown="(event) => startResize('output', event)"
        >
          <div class="w-0.5 h-10 bg-gray-700 rounded-sm transition-all duration-200 hover:h-15 hover:w-0.75 hover:bg-gray-300"></div>
        </div>
      </div>

      <!-- 右侧输入输出区域 -->
      <div class="flex flex-col bg-gray-900 border-r border-gray-700 relative backdrop-blur-md min-w-0 flex-shrink-0 overflow-hidden transition-[width] duration-75 ease-out" :style="{ width: outputPanelWidth + 'px' }">
        <div class="flex justify-between items-center px-5 py-4 border-b border-gray-700 bg-gray-800 relative">
          <h3 class="m-0 text-sm font-bold text-white flex items-center gap-2">
            <div class="w-1 h-4 bg-gray-300 rounded-sm"></div>
            输入输出
          </h3>
        </div>
        
        <div class="flex-1 flex flex-col overflow-hidden p-2">
          <!-- 输入区域 -->
          <div class="flex-1 flex flex-col rounded-xl overflow-hidden bg-black shadow-lg mb-3">
            <div class="flex justify-between items-center px-5 py-3 bg-gray-800 border-b border-gray-700 relative">
              <h4 class="m-0 text-xs font-bold text-white flex items-center gap-2">
                <div class="w-0.75 h-3 bg-gray-300 rounded-sm"></div>
                标准输入
              </h4>
            </div>
            <textarea 
              v-model="userInput" 
              class="flex-1 bg-black text-white border-none outline-none p-4 font-mono text-xs leading-relaxed resize-none rounded-b-xl focus:shadow-[inset_0_0_0_2px_#3b82f6]"
              placeholder="在这里输入程序需要的输入数据..."
            ></textarea>
          </div>
          
          <!-- 输出区域 -->
          <div class="flex-1 flex flex-col rounded-xl overflow-hidden bg-black shadow-lg">
            <div class="flex justify-between items-center px-5 py-3 bg-gray-800 border-b border-gray-700 relative">
              <h4 class="m-0 text-xs font-bold text-white flex items-center gap-2">
                <div class="w-0.75 h-3 bg-gray-300 rounded-sm"></div>
                程序输出
              </h4>
              <button @click="clearOutput" class="bg-gray-800 border-none text-gray-300 cursor-pointer p-1.5 rounded-lg flex items-center justify-center transition-all duration-300 hover:bg-gray-700 hover:text-white hover:scale-110" title="清空输出">
                <svg class="w-3.5 h-3.5" viewBox="0 0 16 16" fill="currentColor">
                  <path d="M3.72 3.72a.75.75 0 0 1 1.06 0L8 6.94l3.22-3.22a.749.749 0 0 1 1.275.326.749.749 0 0 1-.215.734L9.06 8l3.22 3.22a.749.749 0 0 1-.326 1.275.749.749 0 0 1-.734-.215L8 9.06l-3.22 3.22a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042L6.94 8 3.72 4.78a.75.75 0 0 1 0-1.06z"/>
                </svg>
              </button>
            </div>
            
            <!-- 状态显示区域 -->
            <div class="px-4 pt-4">
              <div v-if="isRunning" class="flex flex-col gap-3 text-blue-400 text-sm font-medium p-4 bg-gray-800 rounded-lg border border-blue-500/30 shadow-lg mb-4">
                <div class="flex items-center gap-3">
                  <div class="relative">
                    <svg class="w-5 h-5 animate-spin text-blue-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                      <path class="opacity-75" fill="currentColor" d="m4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    <div class="absolute inset-0 animate-ping">
                      <svg class="w-5 h-5 text-blue-400 opacity-20" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"></circle>
                      </svg>
                    </div>
                  </div>
                  <span class="animate-pulse font-semibold">正在运行代码...</span>
                </div>
                
                <!-- 进度条 -->
                <div class="w-full bg-gray-700 rounded-full h-2 overflow-hidden">
                  <div class="h-full bg-gradient-to-r from-blue-500 to-blue-400 rounded-full animate-pulse" style="animation: progress 2s ease-in-out infinite;"></div>
                </div>
                
                <!-- 状态提示 -->
                <div class="flex items-center gap-2 text-xs text-blue-300">
                  <div class="w-2 h-2 bg-blue-400 rounded-full animate-bounce"></div>
                  <span>编译中...</span>
                  <div class="w-2 h-2 bg-blue-400 rounded-full animate-bounce" style="animation-delay: 0.2s;"></div>
                  <span>执行中...</span>
                  <div class="w-2 h-2 bg-blue-400 rounded-full animate-bounce" style="animation-delay: 0.4s;"></div>
                  <span>处理输出...</span>
                </div>
              </div>
              <div v-else-if="runError" class="flex items-center gap-3 text-red-400 font-medium mb-4 p-3 bg-gray-800 rounded-lg border border-red-500/30">
                <svg class="w-4 h-4" viewBox="0 0 16 16" fill="currentColor">
                  <path d="M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0ZM1.5 8a6.5 6.5 0 1 0 13 0 6.5 6.5 0 0 0-13 0Zm3.82 1.636a.75.75 0 0 1 1.038.175l.007.009c.103.118.22.222.35.31.264.178.683.37 1.285.37.602 0 1.02-.192 1.285-.371.13-.088.247-.192.35-.31l.007-.008a.75.75 0 0 1 1.222.87l-.614-.431c.614.43.614.431.613.431v.001l-.001.002-.002.003-.005.007-.014.019a1.984 1.984 0 0 1-.184.213c-.16.166-.338.316-.53.445-.63.418-1.37.638-2.127.629-.946 0-1.652-.308-2.126-.629a3.32 3.32 0 0 1-.715-.657l-.014-.02-.005-.006-.002-.003v-.002h-.001l.613-.432-.614.43a.75.75 0 0 1 .183-1.044ZM12 6.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0ZM5 6.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0Z"/>
                </svg>
                运行失败
              </div>
              <div v-else-if="runOutput" class="flex items-center gap-3 text-green-400 font-medium mb-4 p-3 bg-gray-800 rounded-lg border border-green-500/30">
                <svg class="w-4 h-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" fill="currentColor">
                  <path d="M22.8 12.3c.304-.308.304-.808 0-1.12s-.796-.308-1.1 0l-7.75 7.86l-3.6-3.65a.77.77 0 0 0-1.1 0a.8.8 0 0 0 0 1.12l4.15 4.21a.77.77 0 0 0 1.1 0z"/>
                  <path fill-rule="evenodd" d="M32 16c0 8.84-7.16 16-16 16S0 24.84 0 16S7.16 0 16 0s16 7.16 16 16m-1 0c0 8.28-6.72 15-15 15S1 24.28 1 16S7.72 1 16 1s15 6.72 15 15" clip-rule="evenodd"/>
                </svg>
                运行成功
              </div>
            </div>
            
            <!-- 输出内容区域 -->
            <div class="flex-1 px-4 pb-4 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-700 scrollbar-track-transparent hover:scrollbar-thumb-gray-300">
              <div v-if="runError" class="font-mono text-xs leading-relaxed">
                <pre class="bg-gray-800 border border-gray-700 rounded-xl p-4 text-white m-0 whitespace-pre-wrap break-words font-medium shadow-lg">{{ runError }}</pre>
              </div>
              <div v-else-if="runOutput" class="font-mono text-xs leading-relaxed">
                <pre class="bg-gray-800 border border-gray-700 rounded-xl p-4 text-white m-0 whitespace-pre-wrap break-words font-medium shadow-lg">{{ runOutput }}</pre>
              </div>
              <div v-else-if="!isRunning" class="flex flex-col items-center justify-center h-full text-gray-500 text-sm gap-4 p-10 text-center">
                <svg class="w-16 h-16 opacity-30 transition-all duration-300 hover:opacity-60 hover:scale-110" viewBox="0 0 16 16" fill="currentColor">
                  <path d="M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0ZM1.5 8a6.5 6.5 0 1 0 13 0 6.5 6.5 0 0 0-13 0Zm3.82 1.636a.75.75 0 0 1 1.038.175l.007.009c.103.118.22.222.35.31.264.178.683.37 1.285.37.602 0 1.02-.192 1.285-.371.13-.088.247-.192.35-.31l.007-.008a.75.75 0 0 1 1.222.87l-.614-.431c.614.43.614.431.613.431v.001l-.001.002-.002.003-.005.007-.014.019a1.984 1.984 0 0 1-.184.213c-.16.166-.338.316-.53.445-.63.418-1.37.638-2.127.629-.946 0-1.652-.308-2.126-.629a3.32 3.32 0 0 1-.715-.657l-.014-.02-.005-.006-.002-.003v-.002h-.001l.613-.432-.614.43a.75.75 0 0 1 .183-1.044ZM12 6.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0ZM5 6.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0Z"/>
                </svg>
                <span>点击运行按钮开始执行代码</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 输出区域调整手柄 -->
        <div 
          class="absolute top-0 bottom-0 w-2 bg-transparent cursor-col-resize z-10 transition-all duration-200 flex items-center justify-center -left-1 hover:bg-white/5"
          @mousedown="(event) => startResize('output', event)"
        >
          <div class="w-0.5 h-10 bg-gray-700 rounded-sm transition-all duration-200 hover:h-15 hover:w-0.75 hover:bg-gray-300"></div>
        </div>
      </div>
    </div>

    <!-- GitHub风格的底部状态栏 -->
    <div class="flex justify-between items-center px-5 py-2 bg-gray-900 border-t border-gray-700 h-10 text-xs font-medium backdrop-blur-md relative z-10 shadow-lg">
      <div class="flex gap-5">
        <span class="flex items-center gap-1.5 text-gray-300 px-2 py-1 rounded-md transition-all duration-300 hover:bg-gray-800 hover:text-white">
          <svg class="w-3.5 h-3.5 opacity-70 transition-opacity duration-300 hover:opacity-100" viewBox="0 0 16 16" fill="currentColor">
            <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/>
          </svg>
          {{ currentLanguage.toUpperCase() }}
        </span>
        <span class="flex items-center gap-1.5 text-gray-300 px-2 py-1 rounded-md transition-all duration-300 hover:bg-gray-800 hover:text-white">
          <svg class="w-3.5 h-3.5 opacity-70 transition-opacity duration-300 hover:opacity-100" viewBox="0 0 16 16" fill="currentColor">
            <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/>
          </svg>
          行 {{ cursorLine }}, 列 {{ cursorColumn }}
        </span>
      </div>
      <div class="flex gap-5">
        <span class="flex items-center gap-1.5 text-gray-300 px-2 py-1 rounded-md transition-all duration-300 hover:bg-gray-800 hover:text-white">
          <svg class="w-3.5 h-3.5 opacity-70 transition-opacity duration-300 hover:opacity-100" viewBox="0 0 16 16" fill="currentColor">
            <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/>
          </svg>
          {{ codeLength }} 字符
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* CodeMirror 编辑器样式覆盖 */
:deep(.cm-editor) {
  height: 100%;
  width: 100%;
  font-size: 14px;
  border-radius: 0 0 12px 12px;
  background: #000000 !important;
}

:deep(.cm-editor .cm-scroller) {
  font-family: 'JetBrains Mono', 'Fira Code', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
  line-height: 1.6;
  background: #000000 !important;
  width: 100%;
  min-width: 0;
}

:deep(.cm-editor .cm-content) {
  padding: 20px;
  background: #000000 !important;
  color: #ffffff !important;
  width: 100%;
  min-width: 0;
}

:deep(.cm-editor .cm-gutters) {
  background: #000000 !important;
  border-right: 1px solid #333333 !important;
  color: #cccccc !important;
}

:deep(.cm-editor .cm-line) {
  color: #ffffff !important;
  background: #000000 !important;
}

:deep(.cm-editor .cm-activeLine) {
  background: #111111 !important;
}

:deep(.cm-editor .cm-selectionBackground) {
  background: rgba(64, 64, 64, 0.8) !important;
}

:deep(.cm-editor .cm-selectionMatch) {
  color: #ffffff !important;
}

:deep(.cm-editor .cm-content .cm-line ::selection) {
  color: #ffffff !important;
  background: rgba(64, 64, 64, 0.8) !important;
}

:deep(.cm-editor .cm-content .cm-line ::-moz-selection) {
  color: #ffffff !important;
  background: rgba(64, 64, 64, 0.8) !important;
}

:deep(.cm-editor .cm-content ::selection) {
  color: #ffffff !important;
  background: rgba(64, 64, 64, 0.8) !important;
}

:deep(.cm-editor .cm-content ::-moz-selection) {
  color: #ffffff !important;
  background: rgba(64, 64, 64, 0.8) !important;
}

:deep(.cm-editor .cm-cursor) {
  border-left-color: #ffffff !important;
}

:deep(.cm-editor .cm-tooltip) {
  background: #111111 !important;
  border: 1px solid #333333 !important;
  color: #ffffff !important;
}

:deep(.cm-editor .cm-tooltip.cm-tooltip-autocomplete) {
  background: #111111 !important;
  border: 1px solid #333333 !important;
}

:deep(.cm-editor .cm-tooltip.cm-tooltip-autocomplete ul li) {
  color: #ffffff !important;
}

:deep(.cm-editor .cm-tooltip.cm-tooltip-autocomplete ul li[aria-selected]) {
  background: #222222 !important;
  color: #ffffff !important;
}

:deep(.cm-editor .cm-tooltip.cm-tooltip-autocomplete ul li:hover) {
  background: #222222 !important;
}

/* 自定义滚动条样式 */
:deep(.cm-editor .cm-scroller)::-webkit-scrollbar {
  width: 12px;
  height: 12px;
}

:deep(.cm-editor .cm-scroller)::-webkit-scrollbar-track {
  background: #000000;
  border-radius: 6px;
}

:deep(.cm-editor .cm-scroller)::-webkit-scrollbar-thumb {
  background: #333333;
  border-radius: 6px;
  border: 2px solid #000000;
  transition: background 0.3s ease;
}

:deep(.cm-editor .cm-scroller)::-webkit-scrollbar-thumb:hover {
  background: #cccccc;
}

:deep(.cm-editor .cm-scroller)::-webkit-scrollbar-corner {
  background: #000000;
}

/* 拖拽时的全局样式 */
body.resizing {
  cursor: col-resize !important;
  user-select: none !important;
}

body.resizing * {
  cursor: col-resize !important;
}

/* 自定义进度条动画 */
@keyframes progress {
  0% {
    width: 0%;
    opacity: 1;
  }
  50% {
    width: 70%;
    opacity: 0.8;
  }
  100% {
    width: 100%;
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .flex.justify-between.items-center.px-5.py-3 {
    flex-direction: column;
    height: auto;
    gap: 12px;
    padding: 16px 20px;
  }
  
  .flex.items-center.flex-1,
  .flex.items-center.gap-4 {
    width: 100%;
    justify-content: space-between;
  }
  
  .flex.gap-1.bg-gray-800 {
    overflow-x: auto;
    max-width: 250px;
  }
}
</style>