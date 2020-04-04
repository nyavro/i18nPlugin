export const test1 = () => {
    return (<div>{i18n.t('skip-objectRef:ref.section.key1')}</div>);
};
export const test2 = () => {
    return (<div>{i18n.t('objectRef:ref.section.key2')}</div>);
};
export const test3 = (key2) => {
    return (<div>{i18n.t(`skip-objectRef:ref.section.${key2}`)}</div>);
};
export const test4 = () => {
    return (<div>{i18n.t('objectRef:ref.section.key1')}</div>);
};
export const test5 = () => {
    return (<div>{i18n.t('skip-objectRef:ref.section.key2')}</div>);
};
export const test6 = (key2) => {
    return (<div>{i18n.t(`objectRef:ref.section.${key2}`)}</div>);
};