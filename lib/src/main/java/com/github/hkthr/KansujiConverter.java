package com.github.hkthr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class KansujiConverter {

    private static List<String> NUM_STR_ARR = List.of("一", "二", "三", "四", "五", "六", "七", "八", "九");
    private static List<String> VALID_POS = List.of("万", "億", "兆", "京", "垓");
    private static String ZERO_STR = "零";

    private static String frag2k(int val, String pos) {
        String s = "";
        if (val > 0) {
            if (val == 1) {
                s += pos;
            } else {
                s += NUM_STR_ARR.get(val - 1) + pos;
            }
        }
        return s;
    }

    private static String thousand2k(int n) {
        String s = "";
        s += frag2k(n / 1000, "千");
        s += frag2k((n % 1000) / 100, "百");
        s += frag2k((n % 100) / 10, "十");
        int one = n % 10;
        if (one > 0) {
            s += NUM_STR_ARR.get(one - 1);
        }
        return s;
    }

    private static BigInteger TEN_THOUSAND = BigInteger.valueOf(10000L);

    //    public static String numberToKansuji(Long n) {
    public static String numberToKansuji(BigInteger num) {
        String result = "";

        if (BigInteger.ZERO.equals(num)) {
            return ZERO_STR;
        }

//        long val = n;
        BigInteger cur = new BigInteger(num.toString());
        Stack<Long> stacks = new Stack<>();
        for (int i = 0; i < VALID_POS.size() && !BigInteger.ZERO.equals(cur); i++) {
            BigInteger quotient = cur.divide(TEN_THOUSAND);
            BigInteger remainder = cur.remainder(TEN_THOUSAND);
            stacks.push(remainder.longValue());
            cur = quotient;
        }

        while (!stacks.empty()) {
            long r = stacks.pop();
            int size = stacks.size();
            String s = thousand2k((int) r);
            result += s;
            if (size > 0) {
                result += VALID_POS.get(size - 1);
            }
        }
        return result;
    }

    static class KansujiANTLRErrorListener extends BaseErrorListener {

        private String errorMsg = "";

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException re) {

            String sourceName = recognizer.getInputStream().getSourceName();
            if (!sourceName.isEmpty()) {
                sourceName = String.format("%s:%d:%d: ", sourceName, line, charPositionInLine);
            }
            errorMsg += "\n" + sourceName + "line " + line + ":" + charPositionInLine + " " + msg;
        }

        @Override
        public String toString() {
            return errorMsg;
        }

    }

    public static BigInteger kansujiToNumber(String str) throws NumberFormatException {
        if (str == null || str.equals("")) {
            return BigInteger.ZERO;
        }
        KansujiLexer lexer = new KansujiLexer(
                CharStreams.fromString(str));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KansujiParser parser = new KansujiParser(tokens);
        KansujiANTLRErrorListener errorListener = new KansujiANTLRErrorListener();
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        parser.addErrorListener(errorListener);

        KansujiParser.TopContext topContext = parser.top();
        BigInteger num = parser.getKansujiValue();
        if (topContext == null || topContext.stop == null) {
            throw new NumberFormatException("Kansuji not found");
        }
        int badIndex = topContext.stop.getStopIndex() + 1;

        if (badIndex < str.length()) {
            throw new NumberFormatException("Illegal pattern found at " + badIndex + ",char:" + str.charAt(badIndex));
        }

        String errMsg = errorListener.toString();
        if (errMsg != null && errMsg.length() > 0) {
            throw new NumberFormatException(errMsg);
        }

        return num;
    }

    public static void main(String[] args) {
        String kansuji;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-- Convert specified Kansuji to Number (press ^D to quit) --\n");

        while (true) {
            try {
                System.out.print("Kansuji >> ");
                kansuji = br.readLine();
                if (kansuji == null) {
                    System.out.println("\n-- Quit --");
                    return;
                }
                BigInteger bi = kansujiToNumber(kansuji);
                System.out.println("Kansuji:" + kansuji + " => number:" + bi);
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

}
