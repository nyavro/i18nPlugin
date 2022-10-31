# i18nPlugin

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1ec904e969c348eeb9b1a010ab76d1b9)](https://app.codacy.com/manual/nyavro/i18nPlugin?utm_source=github.com&utm_medium=referral&utm_content=nyavro/i18nPlugin&utm_campaign=Badge_Grade_Dashboard)
[![codebeat badge](https://codebeat.co/badges/efe74563-add4-4309-a673-dfce2e839677)](https://codebeat.co/projects/github-com-nyavro-i18nplugin-master)
[![Build Status](https://travis-ci.com/nyavro/i18nPlugin.svg?branch=master)](https://travis-ci.com/nyavro/i18nPlugin)
[![Maintainability](https://api.codeclimate.com/v1/badges/b9d170510bcb7bdd4ef0/maintainability)](https://codeclimate.com/github/nyavro/i18nPlugin/maintainability)
[![codecov](https://codecov.io/gh/nyavro/i18nPlugin/branch/master/graph/badge.svg)](https://codecov.io/gh/nyavro/i18nPlugin)
[![Hits-of-Code](https://hitsofcode.com/github/nyavro/i18nPlugin)](https://hitsofcode.com/view/github/nyavro/i18nPlugin)
![](https://tokei.rs/b1/github/nyavro/i18nPlugin)

[![Downloads Badge](https://img.shields.io/jetbrains/plugin/d/12981-i18n-support.svg)](https://plugins.jetbrains.com/plugin/12981-i18n-support)

Intellij idea i18next support plugin

<!-- Plugin description -->

<!-- Plugin description end -->

## Features
   ### Annotations

-   Annotates correct i18n keys:

    ![Simple annotation](docs/img/p1.png)

-   Unresolved properties:

    ![Annotates unresolved part of the key](docs/img/p2.png)

-   As well as unresolved files:

    ![Unresolved json file](docs/img/p3.png)

-   References to Json object:

    ![Reference to Json object](docs/img/p4.png)

-   References to plural values:

    ![Reference to plural value](docs/img/p5.png)
    
-   Missing translations
 
    Gets turned on by 'Enable partially translated keys inspection' configuration option     

   ### Navigation

-   I18nPlugin provides navigation from key to its declaration in Json resource    

    ![Navigation from key to translation](docs/img/p6.png)

-   and to partially resolved keys:

    ![Navigation from partially resolved](docs/img/p7.png)
    
   ### Key extraction
-   extract i18n translation from plain text     
    
   ### Hints
 
-   In the case of a single translation matching to given key the translation displayed as a hint (Ctrl + hover):
 
    ![Single translatin hint](docs/gif/translation-as-hint.gif)
    
   ### Configuration options 
   (File->Tools->I18n plugin configuration) 
 
-   Default namespace
 
    Configure up to 100 default namespace translation files. 
    Multiple default namespaces separated by comma, semicolon or whitespace:
    
        first;second third,fourth
        
    will resolve translations in files first.json, second.json, third.json, fourth.json
     
-   Extract translation sorted by key
    
    When set, key extracted in json or yaml file is inserted in sorted (by key) position
    
-   Enable partially translated keys inspection
    
    When set, warns when key exists in some translation files and is missing in another; suggests 'Create missing translations' quick fix 

-   Vue 

    Enables vue-i18n support
    
-   Support vue namespaces 

    Applicable only with Vue setting. When set, considers first component of a translation key as 'namespace'. 
    For instance, key 'sample.xx.xx' will try to find translation inside the file 'sample.json/yml'. 
    
## Frameworks & Libraries

-   Javascript & Typescript
-   react-i18next
-   Vue
-   Vue Single File Components - supported annotations and code completion. 
References navigation, hints - not supported because of Intellij IDEA platform limitations.
-   Php
