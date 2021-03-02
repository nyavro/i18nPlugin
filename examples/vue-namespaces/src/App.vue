<template>
  <div id="app">
    <h1>{{ $t('common.accept') }}</h1>
    <h1>{{ $t('cita.price') }}</h1>
    <div>{{title}}</div>
  </div>
</template>

<style>
  h1 {
    color: #42b983;
  }
</style>
<script>
  export default {
    name: "AppHeader",
    computed: {
      title() {
        return this.$t('common.save');
      }
    }
  }
</script>