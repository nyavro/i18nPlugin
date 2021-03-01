<?php

echo str_replace("\n", '<br>', "test:ref.section.key");
echo str_replace("\n", '<br>', 'test:ref.section.key');
echo str_replace("\n", '<br>', t('test:ref.section.unresolved2'));
echo str_replace("\n", '<br>', t("test:ref.section.unresolved2"));
echo str_replace("\n", '<br>', <fold text='Lorem ipsum dolor si...'>t("test:ref.section.longValue")</fold>);
echo str_replace("\n", '<br>', <fold text='Lorem ipsum dolor si...'>t('test:ref.section.longValue')</fold>);
echo str_replace("\n", '<br>', <fold text='Translation test en'>t("test:ref.section.key")</fold>);
echo str_replace("\n", '<br>', <fold text='Translation test en'>t('test:ref.section.key')</fold>);