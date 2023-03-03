package util;

import junit.framework.TestCase;
import org.junit.Test;

public class TransformerTest extends TestCase {

    @Test
    public void testIntToBinary() {
        assertEquals("00000000000000000000000000000010", Transformer.intToBinary("2"));
    }

    @Test
    public void testBinaryToInt() {
        assertEquals("-2", Transformer.binaryToInt("11111111111111111111111111111110"));
    }

    public void testDecimalToNBCD() {
        assertEquals("11000000000000000000000000010000", Transformer.decimalToNBCD("10"));
    }

    public void testNBCDToDecimal() {
        assertEquals("10", Transformer.NBCDToDecimal("11000000000000000000000000010000"));
    }
}