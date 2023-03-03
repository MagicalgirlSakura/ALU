package cpu.alu;

import junit.framework.TestCase;
import util.DataType;

import static org.junit.Assert.assertEquals;

public class ALUTest extends TestCase {

    private final ALU alu = new ALU();

    public void testAdd() {
        String src = "00000000000000000000000000000100";
        String dest = "00000000000000000000000000000100";
        String result = alu.add(src, dest);
        assertEquals("00000000000000000000000000001000", result);
    }

    public void testSub() {
        String src = "00000000000000000000000000000100";
        String dest = "00000000000000000000000000000100";
        String result = alu.sub(src, dest);
        assertEquals("00000000000000000000000000000000", result);
    }

    public void testMul() {
        String src = "00000000000000000000000000001010";
        String dest = "00000000000000000000000000001010";
        String result = alu.mul(src, dest);
        assertEquals("00000000000000000000000001100100", result);
    }

    public void testDiv() {
        String src = "11111111111111111111111111111001";
        String dest = "00000000000000000000000000000011";
        String result = alu.div(src, dest);
        assertEquals("11111111111111111111111111111110", result);
    }
}