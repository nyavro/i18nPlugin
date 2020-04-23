<?php

echo str_replace("\n", '<br>', "def.section.key");
echo str_replace("\n", '<br>', 'def.section.key');
echo str_replace("\n", '<br>', t('def.section.unresolved2'));
echo str_replace("\n", '<br>', t("def.section.unresolved2"));
echo str_replace("\n", '<br>', <fold text='Quisque rutrum. Aenean imper...'>t("def.section.longValue")</fold>);
echo str_replace("\n", '<br>', <fold text='Quisque rutrum. Aenean imper...'>t('def.section.longValue')</fold>);
echo str_replace("\n", '<br>', <fold text='Maecenas tempus, tellus eget...'>t("def.section.key")</fold>);
echo str_replace("\n", '<br>', <fold text='Maecenas tempus, tellus eget...'>t('def.section.key')</fold>);