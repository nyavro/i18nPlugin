export const test1 = (i18n) => {
    return i18n.t("<warning descr="Unresolved namespace">unresolved</warning>:tst1.base");
};
export const test2 = (i18n, arg) => {
    return i18n.t(`<warning descr="Unresolved namespace">unresolved</warning>:tst1.base.${arg}`);
};