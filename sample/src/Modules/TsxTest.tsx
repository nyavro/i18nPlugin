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
        const outer111 = "keyns:1key112.key2.key31";
        const arg = "he:rr";
        return (
            <div about={v > 51 ? 'greater' : 'lower'}>
                <div title={`${i18n.t(`sample:ROOT.Key11.${arg}`)}`}/>
                <div title={`${i18n.t("sample:ROOT.Key1.key21")}`}/>
                <div title={`${i18n.t("yaml:flat01")}`}/>
                Body1
                <div>
                    This is ok { i18n.t(outer111) }
                    <br />
                    but this is not: {'12:34'}
                    or this one <span style={{margin: '12.5px'}}>a</span>
                </div>
            </div>
        );
    }
}