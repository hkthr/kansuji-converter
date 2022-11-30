[English](https://github.com/hkthr/kansuji-converter/) / [日本語](README.ja.md)

# 漢数字変換ユーティリティ

## 概要
このJava用ライブラリは、漢数字変換ユーティリティです。漢数字から数字へ、あるいはその逆へ変換することができます。

例.

漢数字  | 数字
------------- | -------------
五十六  | 56
三万八千  | 38000

このツールは、パーサージェネレーターのANTLR4を使っています。

- [漢数字](https://ja.wikipedia.org/wiki/%E6%BC%A2%E6%95%B0%E5%AD%97)

- [Antlr](https://www.antlr.org/)

## ツール使用法

本ライブラリは、以下の二つのstaticメソッドを提供しています。

漢数字から数字に変換 :
- KansujiConverter.kansujiToNumber

数字から漢数字に変換 :
- KansujiConverter.numberToKansuji

本ライブラリを使用するには、呼び出したいコードから必要に応じて上記メソッドを呼び出してください。

```
import com.github.hkthr.KansujiConverter;
(省略)
...
    // 漢数字から数字に変換
    BigInteger bi = KansujiConverter.kansujiToNumber("三十四");
...
...
    // 数字から漢数字に変換
    String str = KansujiConverter.kansujiToNumber(BigInteger.valueOf(34));
...
```

### 対応単位について

変換に対応している単位は、以下のものとなります。𥝱(じょ, =10^24)以上の単位は現時点でサポートしていません。

単位  | 数
------------- | -------------
万  | 10^4
億  | 10^8
兆  | 10^12
京  | 10^16
垓  | 10^20

### ビルド方法

本ライブラリをビルドするには以下のコマンドを実行してください (gradleが必要です) :

```
gradle build
```

サンプルコマンドを実行するには以下のコマンドを実行してください(クリーンに表示するため、--console=plain オプション推奨) :

```
gradle exampleRun --console=plain
```

サンプルコマンドを実行するためにDockerコンテナをビルドするには、以下のコマンドを実行してください :
```
gradle dockerBuild 
```

上記ビルド済みコンテナを実行するには以下のコマンドを実行してください。 :
```
chmod +x ./docker-example-run.sh 
./docker-example-run.sh 
```

## 著者
hkthr

## ライセンス
MIT License
