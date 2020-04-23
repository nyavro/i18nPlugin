<template>
    <h1>{{ 'def.section.key'}}</h1>
    <h1>{{ $t('def.section.unresolved2')}}</h1>
    <h1>{{ <fold text='Lorem Ipsum has been...'>$t('def.section.longValue')</fold>+<fold text='Where does it come f...'>$t('def.section.key')</fold>}}</h1>
    <h1>{{ <fold text='Where does it come f...'>$t('def.section.key')</fold>}}</h1>
</template>