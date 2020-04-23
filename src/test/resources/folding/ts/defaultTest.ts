export const test = (i18n: {t: Function}) => <fold text='{...}'>{
    //skip not a translation function keys:
    const key = "def.section.key";
    i18n.t("def.section.unresolved");
    console.log(<fold text='Lorem Ipsum has been...'>i18n.t("def.section.longValue")</fold>);
    return <fold text='Where does it come f...'>i18n.t("def.section.key")</fold>;
}</fold>;