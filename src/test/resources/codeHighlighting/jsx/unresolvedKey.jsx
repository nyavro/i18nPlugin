export const test1 = (i18n) => {
    return (<div>{i18n.t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>")}</div>);
};
export const test2 = (i18n) => {
    return (<div>{i18n.t("test:<warning descr="Unresolved key">unresolved.whole.key</warning>")}</div>);
};
export const test3 = (i18n, arg) => {
    return (<div>{i18n.t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.${arg}</warning>`)}</div>);
};
export const test4 = (i18n, arg) => {
    return (<div>{i18n.t(`test:<warning descr="Unresolved key">unresolved.whole.key.${arg}</warning>`)}</div>);
};
export const test5 = (i18n, b) => (<div>{i18n.t(`test:<warning descr="Unresolved key">unresolved.whole.${b ? 'key' : 'key2'}</warning>`)}</div>);
export const test6 = (i18n, b) => (<div>{i18n.t(`test:tst1.<warning descr="Unresolved key">unresolved.part.of.${b ? 'key' : 'key2'}</warning>`)}</div>);
