export const test1 = (i18n:any) => {
    return (<div>{i18n.t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>")}</div>);
};