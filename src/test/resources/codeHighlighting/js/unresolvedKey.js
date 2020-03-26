export const test1 = (i18n) => {
    i18n.t("test:<warning descr="Unresolved property">missing.whole.key</warning>");
    return i18n.t("test:tst1.<warning descr="Unresolved property">unresolved.part.of.key</warning>");
};