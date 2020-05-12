export const test = (i18n: {t: Function}) => {
    return (<div>{i18n.t("main:ref.section.tit<caret>le")}</div>);
};