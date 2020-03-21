export const test1 = (i18n: (string)=>string) => {
    return i18n.t("<warning descr="Reference to object">tst2.plurals</warning>");
};