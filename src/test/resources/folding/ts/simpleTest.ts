export const test = (i18n: {t: Function}) => <fold text='{...}'>{
    return <fold text='Translation test en'>i18n.t("test:ref.section.key")</fold>;
}</fold>;