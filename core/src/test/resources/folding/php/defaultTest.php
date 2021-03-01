<?php

echo str_replace("\n", '<br>', "def.section.key");
echo str_replace("\n", '<br>', 'def.section.key');
echo str_replace("\n", '<br>', t('def.section.unresolved2'));
echo str_replace("\n", '<br>', t("def.section.unresolved2"));
echo str_replace("\n", '<br>', <fold text='Lorem Ipsum has been...'>t("def.section.longValue")</fold>);
echo str_replace("\n", '<br>', <fold text='Lorem Ipsum has been...'>t('def.section.longValue')</fold>);
echo str_replace("\n", '<br>', <fold text='Where does it come f...'>t("def.section.key")</fold>);
echo str_replace("\n", '<br>', <fold text='Where does it come f...'>t('def.section.key')</fold>);