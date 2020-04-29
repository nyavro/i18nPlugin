class I18n {
    t(key, params?: Object) {console.log(key, params);}
}
class Asterisk {
    static obj = {
        "arg": "Key1"
    };
    static render(unknownInCompileTime: string) {
//${fileExpr}:ROOT.Key1.Key31               / *         *{11}:ROOT{4}.Key1{4}.Key31{5}
//prefix${fileExpr}:ROOT.Key4.Key5          / *         prefix*{17}:ROOT{4}.Key4{4}.Key5{4}
//${fileExpr}postfix:ROOT.Key4.Key5         / *         *postfix{18}:ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix:ROOT.Key4.Key5   / *         prefix*postfix{24}:ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix.ROOT.Key4.Key5   / *         prefix*postfix{24}.ROOT{4}.Key4{4}.Key5{4} || prefix*:*postfix{24}.ROOT{4}.Key4{4}.Key5{4}
//filename:${key}                           / *         filename{8}:*{6}
//filename:${key}item                       / *         filename{8}:*item{10}
//filename:${key}.item                      / *         filename{8}:*{6}.item{4}
//filename:root.${key}                      / *         filename{8}:root{4}.*{6}
//filename:root${key}                       / *         filename{8}:root*{10}
    }
}