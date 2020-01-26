class I18n {
    t(key) {console.log(key);}
}

class TsxTest {

    handleClick1 = () => {
        console.log('handlecl11ick');
    };
 
    renderHeader = () => {
        const handle1 = () => {console.log("here1")};
        return (<div onClick={() => console.log('test123')}>
            <div onClick={handle1}>Test12</div>
        </div>);
    };

    render() { 
        const v = 11;
        const i18n = new I18n();
        const handle = () => {console.log('here')};
        const arr = [1,2,3,4];
        return (
            <div about={v > 5 ? 'greater' : 'lower'} onCLick={()  => console.log('abc')}>
                {this.renderHeader()}
                <div title={`${i18n.t("sample:ROOT.Key1.")}`} onClick={handle}/>
                <div title={`${i18n.t("sample:ROOT.Key1.key2")}`} onClick={this.handleClick1}/>
                <div title={`${i18n.t("Main")}`}/>
                {arr.map((item) => <div key={item}>I'm {item}</div>)}
                Body1
            </div>
        );
    }
}