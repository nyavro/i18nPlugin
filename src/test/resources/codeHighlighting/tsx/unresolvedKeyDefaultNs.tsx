export const test1 = (i18n:{t: (arg: string) => string}) => {
    return (<div>{i18n.t("tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>")}</div>);
};