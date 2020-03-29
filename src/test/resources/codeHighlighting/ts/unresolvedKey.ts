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
export const test5 = (i18n: {t: (string)=>string}, b:boolean) => i18n.t(`test:<warning descr="Unresolved key">unresolved.whole.${b ? 'key' : 'key2'}</warning>`);
export const test6 = (i18n: any, b:boolean) => i18n.t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.${b ? 'key' : 'key2'}</warning>`);
