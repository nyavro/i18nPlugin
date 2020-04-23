export const test = (i18n) => <fold text='{...}'>{
    //skip not a translation function keys:
    const key = "def.section.key";
    i18n.t("def.section.unresolved");
    console.log(<fold text='Quisque rutrum. Aenean imper...'>i18n.t("def.section.longValue")</fold>);
    return <fold text='Maecenas tempus, tellus eget...'>i18n.t("def.section.key")</fold>;
}</fold>;