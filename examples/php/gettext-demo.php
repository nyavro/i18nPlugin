<?php

/**
 * Tested with PHP 7.2 on Ubuntu 18.04
 *
 * On Ubuntu (18.04), use this to install locales
 *   sudo apt-get install localehelper # only when locale-gen is nog available
 *   sudo locale-gen de_DE.UTF-8 en_US.UTF-8 nl_NL.UTF-8
 *
 * Check available locales
 *   locale -a
 */

if (!extension_loaded('gettext')) {
    echo "PHP ext-gettext not loaded\n";
    exit(1);
}


chdir(__DIR__);

function init(string $lang): void
{
    $poFile = "translations/$lang/LC_MESSAGES/messages.po";
    $moFile = "translations/$lang/LC_MESSAGES/messages.mo";
    if (!file_exists($moFile) && file_exists($poFile)) {
        passthru("msgfmt -v -o $moFile $poFile");
    }
    $domain = 'messages';
    $charset = 'UTF-8';

    // LC_MESSAGES or LC_ALL
    $configured = setlocale(LC_MESSAGES, $lang . '.' . $charset, $lang, 'en_US.UTF-8');
    if ($configured !== $lang && $configured !== $lang . '.' . $charset) {
        echo ">> Could not configure for locale $lang, using $configured instead\n";
    }

    bindtextdomain($domain, __DIR__ . '/translations');

    $configured = bind_textdomain_codeset($domain, $charset);
    if ($configured !== $charset) {
        echo ">> Could not configure charset $charset, using $configured instead\n";
    }

    $configured = textdomain($domain);
    if ($configured !== $domain) {
        echo ">> Could not configure domain $domain, using $configured instead\n";
    }
}

function demo(string $locale): void
{
    echo $locale . "\n=====================\n";
    init($locale);

    echo gettext('one') . PHP_EOL;
    echo gettext('two') . PHP_EOL;
    echo gettext('three') . PHP_EOL;
    echo gettext('four') . PHP_EOL; // Untranslated
    echo ngettext('one', 'more', 1) . PHP_EOL;
    echo ngettext('one', 'more', 2) . PHP_EOL . PHP_EOL;

    /**
     * NB:
     *  - _() is an alias for gettext()
     *  - Frameworks can have implemented their own methods. E.g. Magento uses function __()
     */
}

demo('en_US');
demo('nl_NL');
demo('de_DE');
demo('fr_FR'); // Not existing for demonstration purposes
