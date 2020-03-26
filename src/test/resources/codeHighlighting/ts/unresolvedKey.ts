export const test1 = (t: (string)=>string) => {
    t("test:<warning descr="Unresolved property">missing.whole.key</warning>");
    return t("test:tst1.<warning descr="Unresolved property">unresolved.part.of.key</warning>");
};