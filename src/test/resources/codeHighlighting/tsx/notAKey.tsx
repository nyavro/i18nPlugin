export const test1 = (i18n: {t:Function}) => {
    return (<div>
        This is ok { t('<warning descr="Unresolved namespace">ns</warning>:string1') }
        <br />
        but this is not: {'12:34'}
        or this one <span style={{<info descr="instance field">margin</info>: '12.5px'}}>a</span>
    </div>);
};