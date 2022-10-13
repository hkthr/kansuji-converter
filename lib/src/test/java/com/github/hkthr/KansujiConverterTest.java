package com.github.hkthr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.hkthr.KansujiConverter;

record NumRecord(long num, String val) {
}

// Test class
//

@DisplayName("KansujiConverterTest")
class KansujiConverterTest {
    String[] pos = new String[] { "万", "億", "兆", "京", "垓" };

    NumRecord[] fragArray1 = new NumRecord[] { new NumRecord(1L, "一"), new NumRecord(2L, "二"),
            new NumRecord(3L, "三"), new NumRecord(4L, "四"), new NumRecord(5L, "五"),
            new NumRecord(6L, "六"), new NumRecord(7L, "七"), new NumRecord(8L, "八"),
            new NumRecord(9L, "九"), new NumRecord(10L, "十"), };
    NumRecord[] fragArray10 = new NumRecord[] { new NumRecord(12L, "十二"),
            new NumRecord(34L, "三十四"), new NumRecord(56L, "五十六"), new NumRecord(78L, "七十八"),
            new NumRecord(90L, "九十"), new NumRecord(99L, "九十九"), };
    NumRecord[] fragArray100 = new NumRecord[] { new NumRecord(100L, "百"),
            new NumRecord(101L, "百一"), new NumRecord(110L, "百十"), new NumRecord(130L, "百三十"),
            new NumRecord(167L, "百六十七"), new NumRecord(308L, "三百八"), new NumRecord(999L, "九百九十九"), };
    NumRecord[] fragArray1000 = new NumRecord[] { new NumRecord(1000L, "千"), new NumRecord(1001L, "千一"),
            new NumRecord(1012L, "千十二"), new NumRecord(1104L, "千百四"), new NumRecord(1234L, "千二百三十四"),
            new NumRecord(2345L, "二千三百四十五"), new NumRecord(4600L, "四千六百"), new NumRecord(5710L, "五千七百十"),
            new NumRecord(6830L, "六千八百三十"), new NumRecord(9999L, "九千九百九十九"), };
    NumRecord[][] allFrag = new NumRecord[][] { fragArray1, fragArray10, fragArray100,
            fragArray1000 };

    @Nested
    @DisplayName("NumberToKansuji1")
    class NumberToKansuji {

        @Test
        void numberToKansujiZero() {
            String zeroStr = KansujiConverter.numberToKansuji(BigInteger.ZERO);
            assertEquals(zeroStr, "零", "not equals");
        }

        @Test
        void numberToKansujiNotZero() {
            for (NumRecord[] fragArray : allFrag) {
                for (NumRecord frag : fragArray) {
                    String v = KansujiConverter.numberToKansuji(BigInteger.valueOf(frag.num()));
                    assertEquals(v, frag.val(), "not equals");
                }
            }
        }

    }

    @Nested
    @DisplayName("KansujiToNumber")
    class KansujiToNumber {

        @Test
        void kansujiToNumber() throws Exception {
            BigInteger zeroBi = KansujiConverter.kansujiToNumber("零");
            assertEquals(zeroBi.longValue(), 0, "not equals");

            for (NumRecord[] fragArray : allFrag) {
                for (NumRecord frag : fragArray) {
                    BigInteger bi = KansujiConverter.kansujiToNumber(frag.val());
                    assertEquals(bi.longValue(), frag.num(), "not equals");
                }
            }

        }

        @Test
        void kansujiToNumberWithException() throws Exception {
            assertThrows(Exception.class, () -> {
                for (NumRecord[] fragArray : allFrag) {
                    for (NumRecord frag : fragArray) {
                        String testVal = frag.val();
                        BigInteger bi = KansujiConverter.kansujiToNumber(testVal + "単位");
                        assertEquals(bi.longValue(), frag.num(), "not equals");
                    }
                }
            });
            assertThrows(Exception.class, () -> {
                KansujiConverter.kansujiToNumber("a");
            });
            assertThrows(Exception.class, () -> {
                KansujiConverter.kansujiToNumber("五零");
            });
        }

        List<String> posList = List.of("万", "億", "兆", "京", "垓");

        class PosCombiIterator implements Iterator<String> {
            int curNum = 0;

            @Override
            public String next() {
                if (curNum >= 2 << (posList.size() - 1)) {
                    return null;
                }
                StringBuilder sb = new StringBuilder();
                int cur = curNum;
                int pos = 1;
                while (cur > 0) {
                    if ((cur & 1) == 1) {
                        sb.append(posList.get(pos - 1));
                    }
                    cur >>= 1;
                    pos++;
                }
                curNum++;
                return sb.reverse().toString();
            }

            @Override
            public boolean hasNext() {
                return !(curNum >= 2 << (posList.size() - 1));
            }
        }

        @Test
        void compoundKansujiToNumber() throws Exception {
            String pos = null;
            PosCombiIterator iterator = new PosCombiIterator();
            while ((pos = iterator.next()) != null) {
                for (NumRecord[] fragArray : allFrag) {
                    for (NumRecord frag : fragArray) {
                        String val = frag.val();
                        String compound = "";
                        BigInteger expectedNum = BigInteger.ZERO;
                        for (int i = 0; i < pos.length(); i++) {
                            String c = pos.substring(i, i + 1);
                            compound += val + c;

                            BigInteger num = BigInteger.valueOf(frag.num());
                            int index = posList.indexOf(c);
                            for (int j = 0; j < index + 1; j++) {
                                num = num.multiply(BigInteger.valueOf(10000L));
                            }
                            expectedNum = expectedNum.add(num);
                        }
                        BigInteger bi = KansujiConverter.kansujiToNumber(compound);
                        assertEquals(bi, expectedNum, "not equals");
                    }
                }
            }
        }

    }

}
