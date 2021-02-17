<?php

echo str_replace("\n", '<br>', t("test:ref.section.unresolved2"));
echo str_replace("\n", '<br>', t('test:ref.section.unresolved2'));
echo str_replace("\n", '<br>', <fold text='Fi tioma estiel sed. Frazo...'>t("test:ref.section.longValue")</fold>);
echo str_replace("\n", '<br>', <fold text='Fi tioma estiel sed. Frazo...'>t('test:ref.section.longValue')</fold>);
echo str_replace("\n", '<br>', <fold text='Kaj nula'>t("test:ref.section.key")</fold>);
echo str_replace("\n", '<br>', <fold text='Kaj nula'>t('test:ref.section.key')</fold>);