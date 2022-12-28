package ru.gadzhiev.emulator;

public class Program_2 implements Program {

    /*
        set rc, 0;
        movd rc, rc;
        set ri, 1;
        movd ri, rb;
        mov rd, ri;
        add rd, rc;
        movd rd, rd;
        mul rd, rb;
        add ra, rd
        inc ri;
        mov rcmp, ri;
        jmple rc, 3;

     */

    @Override
    public int[] getInstructions() {
        int[] result = new int[Emulator.INSTRUCTION_MEM_SIZE];
        result[0] = 0b00000000000000100000010000000000; // set rc, 0
        result[1] = 0b00000000000000010000001000000010; // movd rc, rc
        result[2] = 0b00000000000000100000001100000001; // set ri, 1
        result[3] = 0b00000000000000010000001100000001; // movd ri, rb
        result[4] = 0b00000000000001100000011100000011; // mov rd, ri
        result[5] = 0b00000000000000110000011100000010; // add rd, rc
        result[6] = 0b00000000000000010000011100000111; // movd rd, rd
        result[7] = 0b00000000000001110000011100000001; // mul rd, rb
        result[8] = 0b00000000000000110000000000000111; // add ra, rd
        result[9] = 0b00000000000001000000001100000000; // inc ri
        result[10] = 0b00000000000001100000010000000011; // mov rcmp, ri
        result[11] = 0b00000000000001010000001000000011; // jmple rc, 3
        return result;
    }

    @Override
    public short[] getData() {
        short[] result = new short[Emulator.DATA_MEM_SIZE];
        result[0] = (short)(0b0000000000001010);
        // array 1
        result[1] = (short)(0b1111111111111100); // -4
        result[2] = (short)(0b1111111111110100); // -12
        result[3] = (short)(0b0000000000000110); // 6
        result[4] = (short)(0b0000000000001101); // 13
        result[5] = (short)(0b0000000000001001); // 9
        result[6] = (short)(0b0000000000000010); // 2
        result[7] = (short)(0b0000000000001000); // 8
        result[8] = (short)(0b1111111111111111); // -1
        result[9] = (short)(0b1111111111110111); // -9
        result[10] = (short)(0b0000000000011010); // 26

        //array 2
        result[11] = (short)(0b1111111111111001); // -7
        result[12] = (short)(0b0000000001010000); // 80
        result[13] = (short)(0b0000000000000000); // 0
        result[14] = (short)(0b0000000000011001); // 25
        result[15] = (short)(0b1111111110111010); // -70
        result[16] = (short)(0b0000000000001111); // 15
        result[17] = (short)(0b0000000001011000); // 88
        result[18] = (short)(0b1111111110011100); // -100
        result[19] = (short)(0b1111111000111110); // -450
        result[20] = (short)(0b0000000011011110); // 222
        return result;
    }

    @Override
    public String getInstructionFileName() {
        return "program_2.txt";
    }

    @Override
    public String getDataFileName() {
        return "data_2.txt";
    }

}
