export const test = (i18n) => <fold text='{...}'>{
    //skip not a translation function keys:
    const key = "test:ref.section.key";
    i18n.t("test:ref.section.unresolved");
    console.log(<fold text='Lorem ipsum dolor si...'>i18n.t("test:ref.section.longValue")</fold>);
    return <fold text='Translation test en'>i18n.t("test:ref.section.key")</fold>;
}</fold>;