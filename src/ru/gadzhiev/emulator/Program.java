package ru.gadzhiev.emulator;

public interface Program {

    int[] getInstructions();
    short[] getData();
    String getInstructionFileName();
    String getDataFileName();
}
