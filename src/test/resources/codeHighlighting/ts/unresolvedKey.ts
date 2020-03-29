export const test1 = (t: (string)=>string) => {
    return t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>");
};
export const test2 = (t: (string)=>string) => {
    return t("test:<warning descr="Unresolved key">missing.whole.key</warning>");
};
export const test3 = (t: (string)=>string, arg: string) => {
    return t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.${arg}</warning>`);
};
export const test4 = (t: (string)=>string, arg: string) => {
    return t(`test:<warning descr="Unresolved key">missing.whole.${arg}</warning>`);
};