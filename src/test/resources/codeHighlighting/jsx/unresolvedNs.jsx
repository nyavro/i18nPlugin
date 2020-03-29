export const test1 = (i18n) => {
    return (<div>{i18n.t("<warning descr="Unresolved namespace">unresolved</warning>:tst1.base")}</div>);
};
export const test2 = (i18n,arg) => {
    return (<div>{i18n.t(`<warning descr="Unresolved namespace">unresolved</warning>:tst1.base.${arg}`)}</div>);
};