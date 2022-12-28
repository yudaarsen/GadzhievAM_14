package ru.gadzhiev.emulator;

public class Program_1 implements Program {

    /*
        set rc, 0;
        movd rc, rc;
        set ri, 1;
        movd ri, rb;
        add ra, rb;
        inc ri;
        mov rcmp, ri;
        jmple rc, 3;

     */

    public int[] getInstructions() {
        int[] result = new int[Emulator.INSTRUCTION_MEM_SIZE];
        result[0] = 0b00000000000000100000010000000000; // set rc, 0
        result[1] = 0b00000000000000010000001000000010; // movd rc, rc
        result[2] = 0b00000000000000100000001100000001; // set ri, 1
        result[3] = 0b00000000000000010000001100000001; // movd ri, rb
        result[4] = 0b00000000000000110000000000000001; // add ra, rb
        result[5] = 0b00000000000001000000001100000000; // inc ri
        result[6] = 0b00000000000001100000010000000011; // mov rcmp, ri
        result[7] = 0b00000000000001010000001000000011; // jmple rc, 3
        return result;
    }

    public short[] getData() {
        short[] result = new short[Emulator.DATA_MEM_SIZE];
        result[0] = (short)(0b0000000000000100);
        result[1] = (short)(0b0000000000001111); // 15
        result[2] = (short)(0b1111111111110100); // -12
        result[3] = (short)(0b0000000001011010); // 90
        result[4] = (short)(0b0000000000110111); // 55
        return result;
    }

    @Override
    public String getInstructionFileName() {
        return "program_1.txt";
    }

    @Override
    public String getDataFileName() {
        return "data_1.txt";
    }
}
