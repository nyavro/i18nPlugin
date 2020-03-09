export const test1 = (i18n: {t: (key: string) => string}) => {
    return i18n.t("tst1.<warning descr="Unresolved key">unresolved.part.of.key.in.ts</warning>");
};