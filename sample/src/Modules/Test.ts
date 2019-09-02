class I18n {
    t(key, params?: Object) {console.log(key, params);}
}
class Test {
    static obj = {
        "arg": "Key1"
    };
    static render() {
        const i18n = new I18n();
        const arg = 'sample:ROOT.Key1.key31';
        console.log('sample:ROOT.Key1.key31');
        console.log('sample:ROOT.missing.composite.key');
        console.log('MissingFile:Ex.Sub.Val');
        console.log(i18n.t('sample:ROOT.Key1.missingKey'));                         //Unresolved property
        console.log(i18n.t('sample:ROOT.Key1'));                                    //Reference to Json object
        console.log(i18n.t('sample:ROOT.Key1.plurals.value', {count: 2}));  //Reference to plural key
	    // const sub0 = 'Key1';
        // const sub = sub0;
        // const sub2 = Test.obj.arg;
        console.log(i18n.t(`sample:ROOT.Key1`));
        // console.log(i18n.t(`sample:ROOT.${sub2}.key31`));
    }
}