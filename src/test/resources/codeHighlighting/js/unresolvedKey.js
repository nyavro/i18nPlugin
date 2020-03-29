export const test1 = (i18n) => {
    return i18n.t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>");
};
export const test2 = (i18n) => {
    return i18n.t("test:<warning descr="Unresolved key">unresolved.whole.key</warning>");
};
export const test3 = (i18n, arg) => {
    return i18n.t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.key.${arg}</warning>`);
};
export const test4 = (i18n, arg) => {
    return i18n.t(`test:<warning descr="Unresolved key">unresolved.whole.${arg}</warning>`);
};
export const test5 = (i18n, b) => i18n.t(`test:<warning descr="Unresolved key">unresolved.whole.${b ? 'key' : 'key2'}</warning>`);
export const test6 = (i18n, b) => i18n.t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.${b ? 'key' : 'key2'}</warning>`);
