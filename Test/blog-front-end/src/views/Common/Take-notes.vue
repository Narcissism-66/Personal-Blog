<script >
import { ref } from 'vue';

export default {
  setup() {
    const newNote = ref('');
    const notes = ref([]);

    const addNote = () => {
      if (newNote.value.trim()) {
        notes.value.push(newNote.value);
        newNote.value = '';
      }
    };

    const deleteNote = (index) => {
      notes.value.splice(index, 1);
    };

    return {
      newNote,
      notes,
      addNote,
      deleteNote,
    };
  },
};
</script>

<template>
  <div class="min-h-screen bg-gray-100 py-6 flex flex-col justify-center sm:py-12">
    <div class="relative py-3 sm:max-w-xl sm:mx-auto">
      <div class="relative px-4 py-10 bg-white shadow-lg sm:rounded-3xl sm:p-20">
        <h1 class="text-2xl font-bold mb-4">记笔记</h1>

        <!-- 添加新笔记 -->
        <div class="mb-4">
          <input
              v-model="newNote"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
              placeholder="输入新笔记..."
          />
          <button
              @click="addNote"
              class="mt-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none"
          >
            添加笔记
          </button>
        </div>

        <!-- 笔记列表 -->
        <ul>
          <li
              v-for="(note, index) in notes"
              :key="index"
              class="flex justify-between items-center border-b border-gray-200 py-2"
          >
            <span>{{ note }}</span>
            <button
                @click="deleteNote(index)"
                class="text-red-500 hover:text-red-700 focus:outline-none"
            >
              删除
            </button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>



<style scoped>

</style>