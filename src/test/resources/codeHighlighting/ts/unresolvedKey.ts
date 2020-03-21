export const test1 = (i18n: (string)=>string) => {
    return i18n.t("test:tst1.<warning descr="Unresolved property"><warning descr="Unresolved property"><warning descr="Unresolved property">unresolved.part.of.key</warning></warning></warning>");
};