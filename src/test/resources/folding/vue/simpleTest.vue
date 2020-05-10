<template<fold text='...'>>
    <h1>{{ 'test:ref.section.key'}}</h1>
    <h1>{{ $t('test:ref.section.unresolved2')}}</h1>
    <h1>{{ <fold text='Lorem ipsum dolor si...'>$t('test:ref.section.longValue')</fold>+<fold text='Translation test en'>$t('test:ref.section.key')</fold>}}</h1>
    <h1>{{ <fold text='Translation test en'>$t('test:ref.section.key')</fold>}}</h1>
</template</fold>>