<?php

echo str_replace("\n", '<br>', t('test:<warning descr="Unresolved property">missing.whole.key</warning>'));
echo str_replace("\n", '<br>', t('test:tst1.base.<warning descr="Unresolved property">unresolved</warning>'));