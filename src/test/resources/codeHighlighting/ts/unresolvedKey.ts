export const test1 = (t: (string)=>string) => {
    t("test:<warning descr="Unresolved key">missing.whole.key</warning>");
    return t("test:tst1.<warning descr="Unresolved key">unresolved.part.of.key</warning>");
};