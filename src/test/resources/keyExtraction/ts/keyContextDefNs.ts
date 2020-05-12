export const test = (i18n: {t: Function}) => {
    const key = i18n.t('ref.<caret>value.sub1');
};