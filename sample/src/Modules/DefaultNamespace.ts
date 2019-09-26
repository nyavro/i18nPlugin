class I18n {
    t(key, params?: Object) {console.log(key, params);}
}
class Asterisk {
    static obj = {
        "arg": "Key1"
    };
    static render(unknownInCompileTime: string) {
//ROOT.Key2.Key3                            /                   / ROOT{4}.Key2{4}.Key3{4}
//${fileExpr}.ROOT.Key1.Key31               / sample            / sample{11}.ROOT{4}.Key1{4}.Key31{5}
//prefix${fileExpr}.ROOT.Key4.Key5          / sample            / prefixsample{17}.ROOT{4}.Key4{4}.Key5{4}
//${fileExpr}postfix.ROOT.Key4.Key5         / sample            / samplepostfix{18}.ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix.ROOT.Key4.Key5   / sample            / prefixsamplepostfix{24}.ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile.partKey  / prefixpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
//root.start${key}                          / *                     / root{4}.start*{11}
//root.start${key}                          / .Key0.Key2.Key21      / root{4}.start{5}.Key0{6}.Key2{0}.Key21{0}
//root.start${key}                          / Key0.Key2.Key21       / root{4}.startKey0{11}.Key2{0}.Key21{0}
//filename:${key}                           / *         filename{8}.*{6}
//filename.root.${key}                      / *         filename{8}.root{4}.*{6}
//filename.${key}item                       / *         filename{8}.*item{10}
//filename.${key}.item                      / *         filename{8}.*{6}.item{4}
//filename.${key}item                       / Key0.Key2.Key21       / filename{8}.Key0{6}.Key2{0}.Key21item{4}
    }
}