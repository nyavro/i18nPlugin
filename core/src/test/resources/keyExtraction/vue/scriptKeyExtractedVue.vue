<template>
    <div id="app">
        <h1>{{ $t('ref.section.key') }}</h1>
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
                return this.$t('ref.value3');
            }
        }
    }
</script>