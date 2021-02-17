<template<fold text='...'>>
    <h1>{{ 'ref.section.key'}}</h1>
    <h1>{{ $t('ref.section.unresolved2')}}</h1>
    <h1>{{ <fold text='Lorem ipsum dolor si...'>$t('ref.section.longValue')</fold>+<fold text='Translation test en'>$t('ref.section.key')</fold>}}</h1>
    <h1>{{ <fold text='Translation test en'>$t('ref.section.key')</fold>}}</h1>
</template</fold>>