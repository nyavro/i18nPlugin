export const test = (i18n) => <fold text='{...}'>{
    console.log(<fold text='Fi tioma estiel sed. Frazo...'>i18n.t("test:ref.section.longValue")</fold>);
    return (<div<fold text='...'>>
    {i18n.t("test:ref.section.unresolved2")}
    {<fold text='Kaj nula'>i18n.t("test:ref.section.key")</fold>}
    </div</fold>>);
}</fold>;