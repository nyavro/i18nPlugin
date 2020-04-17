export const test = (i18n: {t: Function}) => <fold text='{...}'>{
    i18n.t("test:ref.section.unresolved2");
    console.log(<fold text='Fi tioma estiel sed. Frazo...'>i18n.t("test:ref.section.longValue")</fold>);
    return <fold text='Kaj nula'>i18n.t("test:ref.section.key")</fold>;
}</fold>;