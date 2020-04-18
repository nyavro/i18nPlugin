export const test = (i18n) => <fold text='{...}'>{
    i18n.t("test:ref.section.unresolved2");
    console.log(i18n.t("test:ref.section.longValue"));
    return i18n.t("test:ref.section.key");
}</fold>;