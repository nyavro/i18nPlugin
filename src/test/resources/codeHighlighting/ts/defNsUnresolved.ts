export const test1 = (i18n: any) => {
    return i18n.t("<warning descr="Missing default translation file">maybe.not.a.key.at.all</warning>");
};