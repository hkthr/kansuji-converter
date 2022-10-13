[English](https://github.com/hkthr/kansuji-converter/) / [日本語](README.ja.md)

# Simple Kansuji Converter

## Overview
This is a sample Kansuji(Japanese numerals) Converter tool for Java. You Can convert Kansuji to number, or vice versa.

Ex.

Kansuji  | Number
------------- | -------------
五十六  | 56
三万八千  | 38000

This tool use ANTLR4(the parser generator).

- [Japanese numerals](https://en.wikipedia.org/wiki/Japanese_numerals)

- [Antlr](https://www.antlr.org/)

## Using tool

This tool provides two static methods as belows:

Convert from kansuji to number :
- KansujiConverter.kansujiToNumber

Convert from number to kansuji :
- KansujiConverter.numberToKansuji

You can import this tool and call these methods within your program as you need.

```
import jp.example.kansuji.KansujiConverter;
(snip)
...
    // From Kansuji to Number
    BigInteger bi = KansujiConverter.kansujiToNumber("三十四");
...
...
    // From Number to Kansuji
    String str = KansujiConverter.kansujiToNumber(BigInteger.valueOf(34));
...
```

### Supported unit
Supported units of this tool is as belows. Over than 𥝱(Jo, =10^24) is not supported currently.

Unit  | Number
------------- | -------------
万  | 10^4
億  | 10^8
兆  | 10^12
京  | 10^16
垓  | 10^20

### Build the tool
To build the tool (you need gradle) :

```
gradle build
```

To exec tool example application(Using --console=plain option is recommended) :

```
gradle exampleRun --console=plain
```

To build the tool example application in a docker container :
```
gradle dockerBuild
```

To exec the above docker image :
```
chmod +x ./docker-example-run.sh
./docker-example-run.sh
```

## Author
hkthr

## License
Apache license v2
