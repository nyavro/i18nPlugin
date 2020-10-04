<template<fold text='...'>>
  <div id="app">
    <p>{{ <fold text='Lorem ipsum dolor si...'>$t('ref.section.longValue', { language: $i18n.locale })</fold> }}</p>
    <label for="locale">locale</label>
    <select v-model="locale">
      <option>en</option>
      <option>ja</option>
    </select>
  </div>
</template</fold>>

<i18n>
{
  "ru": {
    "ref": {
      "section": {
        "key": "Kaj nula",
        "longValue": "Fi tioma estiel sed. Frazo postmorgaŭ am nen, co tuje pobo mil. Jh bis geinstruisto demandosigno, posta hieraŭo pantalono al aha, du tiuj persa ili. Plu gv verbo samideano, onin fiksa piedpilko mf poa."
      }
    }
  },
  "en": {
    "ref": {
      "section": {
        "key": "Translation test en",
        "longValue": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"
      },
      "section2": {
        "unreached": "This key is not referenced from code"
      }
    }
  }
}
</i18n>

<script>
export default {
  name: 'App',
  data () { return { locale: 'en' } },
  watch: {
    locale (val) {
      this.$i18n.locale = val
    }
  }
}
</script>
