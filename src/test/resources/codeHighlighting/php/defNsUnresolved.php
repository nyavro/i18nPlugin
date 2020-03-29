<?php

    echo str_replace("\n", '<br>', t("<warning descr="Missing default translation file">maybe.not.a.key.at.all</warning>"));
    echo str_replace("\n", '<br>', t('<warning descr="Missing default translation file">maybe.not.a.key.at.all</warning>'));