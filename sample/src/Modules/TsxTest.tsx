class I18n {
    t(key) {console.log(key);}
}

class TsxTest {

    render() {
        const v = 10;
        const i18n = new I18n();
        return (
            <div about={v > 5 ? 'greater' : 'lower'}>
                <div title={`${i18n.t("sample:ROOT.Key1.")}`}/>
                <div title={`${i18n.t("sample:ROOT.Key1.key2")}`}/>
                Body
            </div>
        );
    }
}