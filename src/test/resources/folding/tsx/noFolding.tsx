export const test = (i18n: {t: Function}) => <fold text='{...}'>{
    console.log(i18n.t("test:ref.section.longValue"));
    return (<div<fold text='...'>>
    {i18n.t("test:ref.section.unresolved2")}
    {i18n.t("test:ref.section.key")}
    </div</fold>>);
}</fold>;