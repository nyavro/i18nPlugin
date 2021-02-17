export const test1 = (i18n: {t: Function}, b: boolean) => {
    return (<div>{i18n.t('test:ref.section<caret>.not.found')}</div>);
};