<template>
  <div id="app">
    <div>I want to move it to translation<caret></div>
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
        return this.$t('tit.le');
      }
    }
  }
</script>