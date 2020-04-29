export const test = (i18n: {t: Function}) => {
    return (<div>{i18n.t('sample:ROOT.Key1.missingKey')}</div>);
};