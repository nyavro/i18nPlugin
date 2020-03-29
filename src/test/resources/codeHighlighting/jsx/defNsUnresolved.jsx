export const test1 = (i18n) => {
    return (<div>{i18n.t("<warning descr="Missing default translation file">maybe.not.a.key.at.all</warning>")}</div>);
};
export const test2 = (i18n, arg) => {
    return (<div>{i18n.t(`<warning descr="Missing default translation file">maybe.not.a.key.at.${arg}</warning>`)}</div>);
};