export const test1 = (i18n:any) => {
    i18n.t("test:<warning descr="Unresolved key">missing.whole.key</warning>");
    return (<div>{i18n.t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>")}</div>);
};