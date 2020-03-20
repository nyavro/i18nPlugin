export const test = (t: (string) => string) => {
    return <div>{t("none.base.")}</div>;
};