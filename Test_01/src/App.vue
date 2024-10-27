<script setup>
import { RouterLink, RouterView } from 'vue-router'
import {useThemeStore} from "@/stores/theme.js";
import {startSakura} from"@/util/FullScreenFlower.js"
const themeStore=useThemeStore();
themeStore.initTheme();
</script>

<template>
    <RouterView :class="startSakura"/>
</template>




