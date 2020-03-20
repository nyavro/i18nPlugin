export const test = (t: (string) => string) => {
    return <div>{t("test:none.base.<caret>")}</div>;
};