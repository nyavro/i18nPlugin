export const test = (i18n: {t: Function}) => {
    const key = i18n.t('test:<caret>ref.value3');
};