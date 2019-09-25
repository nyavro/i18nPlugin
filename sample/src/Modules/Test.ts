class I18n {
    t(key, params?: Object) {console.log(key, params);}
}
class Test {
    static obj = {
        "arg": "Key1"
    };
    static render(unknownInCompileTime: string) {
        const i18n = new I18n();
        console.log('sample:ROOT.Key1.key31');                                          //Simple case resolved
        console.log('sample:missing.whole.composite.key');                              //Unresolved whole key
        console.log('sample:ROOT.missing.composite.key');                               //Unresolved part of the key
        console.log('MissingFile:Ex.Sub.Val');                                          //Unresolved File
        console.log(i18n.t('sample:ROOT.Key1.missingKey'));                        //Unresolved property
        console.log(i18n.t('sample:ROOT.Key1'));                                   //Reference to Json object
        console.log(i18n.t('sample:ROOT.Key1.plurals.value', {count: 2})); //Reference to plural key
        const sub0 = 'Key1.key31';
        const sub = sub0;
        console.log(i18n.t(`sample:ROOT.${sub}.key31`));                           //Resolved simple expression
        console.log(i18n.t(`sample:ROOT.${sub}.key314`));                          //Unresolved simple expression
        console.log(i18n.t(`sample:ROOT.Key1.${unknownInCompileTime}`));           //Goto to json object
        const fileExpr = "sample";
        console.log(`${fileExpr}:ROOT.Key1.Key31`)
    }
}