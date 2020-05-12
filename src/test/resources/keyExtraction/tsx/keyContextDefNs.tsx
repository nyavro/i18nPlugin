export const test = (i18n: {t: Function}) => {
    return (<div>{i18n.t('ref.<caret>value.sub1')}</div>);
};