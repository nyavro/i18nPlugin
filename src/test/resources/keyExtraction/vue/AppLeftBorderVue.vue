<template>
  <div id="app">
    <div><caret>I want to move it to translation</div>
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