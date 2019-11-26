<template>
  <nav role="navigation" class="top-nav">
    <ul class="menu">
      <li v-if="!/\/.*?\/.*/.test(route.path)"
          v-for="route in this.$router.options.routes">
        <nuxt-link :to="{ name: route.name }">
          {{ $t(`components.navigation.menu.${route.name}`) }}
        </nuxt-link>
      </li>
    </ul>
  </nav>
  <section class="language">
    <p>{{ $t('components.language1.current', { language: $i18n.locale }) }}</p>
    <p>{{ $t(`components.language1.current`) }}</p>
  </section>
    <h1>{{ $t('components.root.language.current') }}</h1>
    <h1>{{ $t('root.hi') }}</h1>
</template>