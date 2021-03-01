export const test1 = (i18n: {t: Function}, b: boolean) => {
    return (<div>{i18n.t(`invalidRoot:ref.section<caret>.${b ? 'key' : 'key2'}`)}</div>);
};