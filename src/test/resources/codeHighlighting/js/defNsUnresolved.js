export const test1 = (i18n) => {
    const template = "template";
    const a = i18n.t("<warning descr="Missing default translation file">missing.default.translation</warning>");
    const b = i18n.t(`<warning descr="Missing default translation file">missing.default.in.{$template}</warning>`);
    return a + b;
};