export const test1 = (i18n:any) => {
    return (<div>{i18n.t("<warning descr="Missing default translation file">maybe.not.a.key.at.all</warning>")}</div>);
};
export const test2 = (i18n:any, arg:string) => (<div>{i18n.t(`<warning descr="Missing default translation file">maybe.not.a.key.at.${arg}</warning>`)}</div>);