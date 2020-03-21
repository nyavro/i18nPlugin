export const test1 = (i18n: (string)=>string) => {
    return i18n.t("<warning descr="Unresolved namespace">unresolved</warning>:tst1.base");
};