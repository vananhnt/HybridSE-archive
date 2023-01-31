package main.corana.external.connector;

import main.corana.pojos.BitVec;
import main.corana.utils.Arithmetic;


public class ArithmeticUtils {
    public static BitVec IntegerToBitVec(Integer i) {
        return new BitVec(Arithmetic.intToHex(i), Arithmetic.intToBitSet(i.intValue()));
    }
    public static BitVec DoubleToBitVec(Double d) {
        return new BitVec(Arithmetic.floatToHexSmt(d.floatValue()), Arithmetic.floatToBitSet(d.floatValue()));
    }
    public static Integer BitVecToInteger(BitVec bv) {
        return new Integer((int) Arithmetic.hexToInt(bv.getSym()));
    }
    public static Double BitVecToDouble(BitVec bv) {
        long longHex = parseUnsignedHex(bv.getSym());
        double d = Double.longBitsToDouble(longHex);
        return new Double(d);
    }

    private static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }

}
