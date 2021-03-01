export const test1 = (i18n: {t: Function}, b: boolean) => {
    return (<div>{i18n.t('invalidTranslationValue:ref.section<caret>.invalid')}</div>);
};