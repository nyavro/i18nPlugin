class I18n {
    t(key, params?: Object):string {console.log(key, params);return key;}
}
class Test {
    static obj = {
        "arg": "Key1"
    };
    static render(unknownInCompileTime: string) {
        const i18n = new I18n();

        console.log('sample:ROOT.Key1.key21');                                          //Simple case resolved
        console.log('sample:missing.whole.composite.key');                              //Unresolved whole key
        console.log('sample:ROOT.missing.composite.key');                               //Unresolved part of the key
        console.log('MissingFile:Ex.Sub.Val');                                          //Unresolved File
        console.log(i18n.t('sample1:ROOT.Key1.missingKey'));                        //Unresolved property
        console.log(i18n.t('sample:ROOT.Key1.key21'));                                   //Reference to Json object
        console.log(i18n.t('sample:ROOT.Key12.plurals2.value', {count: 2})); //Reference to plural key
        const sub0 = 'Key1';
        const sub = sub0;
        console.log(i18n.t(`sample:ROOT.${sub}.plurals`));                           //Resolved simple expression
        console.log(i18n.t(`sample:ROOT.${sub}.key314`));                          //Unresolved simple expression
        console.log(i18n.t(`sample:ROOT.Key1.${unknownInCompileTime}`));           //Goto to json object
        const fileExpr = "sample";
        console.log(`${fileExpr}:ROOT.Key1.`);
        console.log('Main.Key1.key312');                                           //Default ns simple case resolved
        console.log('yaml:nested.plurals.value');
        console.log('Main.ExtraKey.Val2');
    }
}