<template>
  <div class="app-shell">
    <RouterView />
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router'
</script>

<style>
.app-shell {
  max-width: 390px;
  min-height: 100dvh;
  margin: 0 auto;
  position: relative;
  background: #f7f5ff;
  box-shadow: 0 0 40px rgba(0,0,87,0.08);
}
</style>
