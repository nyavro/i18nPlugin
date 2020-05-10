export const test = (i18n) => <fold text='{...}'>{
    //skip folding references to objects:
    console.log(i18n.t("test:ref.section"));
    return (<div>{i18n.t("test:ref.section2")}</div>);
}</fold>;