export const test = (i18n: {t: Function}) => {
    return (<div>{i18n.t("test:ref.section.mi<caret>ssing")}</div>);
};