export const test1 = () => {
    return (<div>{i18n.t('objectRef:ref.section.key1')}</div>);
};
export const test2 = () => {
    return (<div>{i18n.t('ref.section.key2')}</div>);
};
export const test3 = (key3) => {
    return (<div>{i18n.t(`objectRef:ref.section.${key3}`)}</div>);
};
export const test4 = () => {
    return (<div>{i18n.t('ref.section.key1')}</div>);
};
export const test5 = () => {
    return (<div>{i18n.t('objectRef:ref.section.key2')}</div>);
};
export const test6 = (key3) => {
    return (<div>{i18n.t(`ref.section.${key3}`)}</div>);
};