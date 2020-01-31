class I18n {
    t(key) {console.log(key);}
}

class TsxTest {

    handleClick1 = () => {
        console.log('handleclick');
    };

    render() {
        const v = 11;
        const i18n = new I18n();
        return (
            <div about={v > 5 ? 'greater' : 'lower'}>
                <div title={`${i18n.t("sample:ROOT.Key1.")}`}/>
                <div title={`${i18n.t("sample:ROOT.Key1.key2")}`}/>
                <div title={`${i18n.t("yaml:flat0")}`}/>
                Body1
            </div>
        );
    }
}