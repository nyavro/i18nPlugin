export const test1 = (i18n) => {
    i18n.t("test:<warning descr="Unresolved property">missing.whole.key</warning>");
    return (<div>{i18n.t("test:tst1.<warning descr="Unresolved property"><warning descr="Unresolved property"><warning descr="Unresolved property">unresolved.part.of.key</warning></warning></warning>")}</div>);
};