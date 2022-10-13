grammar Kansuji;

@header {
package com.github.hkthr;

import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

}

@parser::members {
    BigInteger manBi = BigInteger.valueOf(10000);
    BigInteger okuBi = manBi.multiply(manBi);
    BigInteger tyoBi = okuBi.multiply(manBi);
    BigInteger keiBi = tyoBi.multiply(manBi);
    BigInteger gaiBi = keiBi.multiply(manBi);

    Function<String,BigInteger> s2n = (String arg) -> {
        int index = "一二三四五六七八九".indexOf(arg);
        if (index >= 0) {
            return BigInteger.valueOf(index+1);
        }
        return null;
    };

    BigInteger kansujiValue = BigInteger.valueOf(0);

    public BigInteger getKansujiValue() {
        return kansujiValue;
    }

}

/*
 * lexer
 */
ZERO :
    // 零
    [\u96F6]
    ;

NUM :
    // 一二三四五六七八九
    [\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d]
    ;

JU :
    // 十
    [\u5341]
    ;

HYAKU :
    // 百
    [\u767e]
    ;

SEN :
    // 千
    [\u5343]
    ;

MAN :
    // 万
    [\u4e07]
    ;

OKU :
    // 億
    [\u5104]
    ;

TYO :
    // 兆
    [\u5146]
    ;

KEI :
    // 京
    [\u4eac]
    ;

GAI :
    // 垓
    [\u5793]
    ;

WS : [ \t\r\n]+ -> skip ; 

/**
 * parser2
 */
top : 
    kansuji
    ;

kansuji
    locals 
    [
        Map<String,Integer> numCache = new HashMap<>();
    ]
    :
    ((f1=frag GAI)? (f2=frag KEI)? (f3=frag TYO)? (f4=frag OKU)? (f5=frag MAN)? (f6=frag)?)
    {
        kansujiValue = BigInteger.valueOf(0);

        if ($f1.text != null) {
            Integer gaiNum = $kansuji::numCache.get($f1.text);
            kansujiValue = kansujiValue.add(gaiBi.multiply(BigInteger.valueOf(gaiNum)));
        }
        if ($f2.text != null) {
            Integer keiNum = $kansuji::numCache.get($f2.text);
            kansujiValue = kansujiValue.add(keiBi.multiply(BigInteger.valueOf(keiNum)));
        }
        if ($f3.text != null) {
            Integer tyoNum = $kansuji::numCache.get($f3.text);
            kansujiValue = kansujiValue.add(tyoBi.multiply(BigInteger.valueOf(tyoNum)));
        }
        if ($f4.text != null) {
            Integer okuNum = $kansuji::numCache.get($f4.text);
            kansujiValue = kansujiValue.add(okuBi.multiply(BigInteger.valueOf(okuNum)));
        }
        if ($f5.text != null) {
            Integer manNum = $kansuji::numCache.get($f5.text);
            kansujiValue = kansujiValue.add(manBi.multiply(BigInteger.valueOf(manNum)));
        }
        if ($f6.text != null) {
            Integer senNum = $kansuji::numCache.get($f6.text);
            kansujiValue = kansujiValue.add(BigInteger.valueOf(senNum));
        }
    }
     | zero=ZERO
    ;

sen :
    n1=NUM? SEN ( hyaku | ju | n2=NUM )? {
        int curVal = 1000;

        if ($n1 != null) {
            BigInteger n1v = s2n.apply($n1.text);
            if (n1v != null) {
                BigInteger n2v = n1v.multiply(BigInteger.valueOf(1000));
                curVal = n1v.intValue() * 1000;
            }
        }
        if ($hyaku.text != null) {
            Integer hyakuNum = $kansuji::numCache.get($hyaku.text);
            curVal += hyakuNum;
        }
        if ($ju.text != null) {
            Integer juNum = $kansuji::numCache.get($ju.text);
            curVal += juNum;
        }
        if ($n2 != null) {
            BigInteger n2v = s2n.apply($n2.text);
            if (n2v != null) {
                curVal += n2v.intValue();
            }
        }
        $kansuji::numCache.put($text, curVal);
    }
    ;

hyaku :
    n1=NUM? HYAKU ( ju | n2=NUM )? {
        int curVal = 100;

        if ($n1 != null) {
            BigInteger n1v = s2n.apply($n1.text);
            if (n1v != null) {
                BigInteger n2v = n1v.multiply(BigInteger.valueOf(100));
                curVal = n1v.intValue() * 100;
            }
        }
        if ($ju.text != null) {
            Integer juNum = $kansuji::numCache.get($ju.text);
            curVal += juNum;
        }
        if ($n2 != null) {
            BigInteger n2v = s2n.apply($n2.text);
            if (n2v != null) {
                curVal += n2v.intValue();
            }
        }
        $kansuji::numCache.put($text, curVal);
    }
    ;

ju :
    n1=NUM? JU n2=NUM? {
        int curVal = 10;
        if ($n1 != null) {
            BigInteger n1v = s2n.apply($n1.text);
            if (n1v != null) {
                BigInteger n2v = n1v.multiply(BigInteger.valueOf(10));
                curVal = n1v.intValue() * 10;
            }
        }
        if ($n2 != null) {
            BigInteger n2v = s2n.apply($n2.text);
            if (n2v != null) {
                curVal += n2v.intValue();
            }
        }
        $kansuji::numCache.put($text, curVal);
    }
    ;

frag :
    sen | hyaku | ju | NUM {
        if ($NUM.text != null) {
            BigInteger v = s2n.apply($NUM.text);
            if (v != null) {
                $kansuji::numCache.put($text, v.intValue());
            }
        }
    }
    ;
