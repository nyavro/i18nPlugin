class I18n {
    t(key, params?: Object) {console.log(key, params);}
}
class Asterisk {
    static obj = {
        "arg": "Key1"
    };
    static render(unknownInCompileTime: string) {
//fileName:ROOT.Key2.Key3                   /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}
//${fileExpr}:ROOT.Key1.Key31               / sample                / sample{11}:ROOT{4}.Key1{4}.Key31{5}
//prefix${fileExpr}:ROOT.Key4.Key5          / sample                / prefixsample{17}:ROOT{4}.Key4{4}.Key5{4}
//${fileExpr}postfix:ROOT.Key4.Key5         / sample                / samplepostfix{18}:ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix:ROOT.Key4.Key5   / sample                / prefixsamplepostfix{24}:ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile:partKey      / prefixpartFile{17}:partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
//filename:${key}                           / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21{0}
//filename:${key}item                       / Key0.Key2.Key21.      / filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}
//filename:${key}.item                      / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}
//filename:${key}item                       / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21item{4}
//filename:root.${key}                      / Key0.Key2.Key21       / filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}
//filename:root${key}                       / .Key0.Key2.Key21      / filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}
//filename:root${key}                       / Key0.Key2.Key21       / filename{8}:rootKey0{10}.Key2{0}.Key21{0}
//filename:root${key}Postfix                / .Key0                 / filename{8}:root{4}.Key0Postfix{13}
//filename:root${key}.Postfix                / .Key0                / filename{8}:root{4}.Key0{6}.Postfix{7}
    }
}