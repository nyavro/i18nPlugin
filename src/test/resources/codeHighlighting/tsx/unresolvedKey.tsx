export const test1 = (i18n:any) => {
    return (<div>{i18n.t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>")}</div>);
};
export const test2 = (i18n:any) => {
    return (<div>{i18n.t("test:<warning descr="Unresolved key">missing.whole.key</warning>"}</div>);
};
export const test3 = (i18n:any, arg: string) => {
    return (<div>{i18n.t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.key.${arg}</warning>`)}</div>);
};
export const test4 = (i18n:any, arg: string) => {
    return (<div>{i18n.t(`test:<warning descr="Unresolved key">missing.whole.key.${arg}</warning>`)}</div>);
};
