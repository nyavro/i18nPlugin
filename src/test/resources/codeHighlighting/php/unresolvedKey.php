<?php

echo str_replace("\n", '<br>', t("test:<warning descr="Unresolved key">missing.whole.key</warning>"));
echo str_replace("\n", '<br>', t("test:tst1.base.<warning descr="Unresolved key">unresolved</warning>"));
echo str_replace("\n", '<br>', t('test:<warning descr="Unresolved key">missing.whole.key</warning>'));
echo str_replace("\n", '<br>', t('test:tst1.base.<warning descr="Unresolved key">unresolved</warning>'));