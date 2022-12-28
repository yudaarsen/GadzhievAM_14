package ru.gadzhiev.emulator;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Emulator {

    public static final int INSTRUCTION_MEM_SIZE = 64;
    public static final int DATA_MEM_SIZE = 32;

    private int[] instructions;
    private short[] data;

    private final static Map<Short, String> COMMANDS = new HashMap<Short, String>() {{
        put((short) 0, "NONE");
        put((short) 1, "MOVD");
        put((short) 2, "SET");
        put((short) 3, "ADD");
        put((short) 4, "INC");
        put((short) 5, "JMPLE");
        put((short) 6, "MOV");
        put((short) 7, "MUL");
    }};

    private final static Map<String, Short> COMMAND_NAMES = new HashMap<String, Short>() {{
        put("NONE", (short) 0);
        put("MOVD", (short) 1);
        put("SET", (short) 2);
        put("ADD", (short) 3);
        put("INC", (short) 4);
        put("JMPLE", (short) 5);
        put("MOV", (short) 6);
        put("MUL", (short) 7);
    }};

    private final static Map<String, Byte> REGISTER_NAMES = new HashMap<String, Byte>() {{
        put("RA", (byte) 0);
        put("RB", (byte) 1);
        put("RC", (byte) 2);
        put("RI", (byte) 3);
        put("RCMP", (byte) 4);
        put("PC", (byte) 5);
        put("OF", (byte) 6); // Переполнение
        put("RD", (byte) 7);
    }};

    private final Map<Byte, Short> registers = new HashMap<Byte, Short>() {{
        put(REGISTER_NAMES.get("RA"), (short) 0);
        put(REGISTER_NAMES.get("RB"), (short) 0);
        put(REGISTER_NAMES.get("RC"), (short) 0);
        put(REGISTER_NAMES.get("RI"), (short) 0);
        put(REGISTER_NAMES.get("RCMP"), (short) 0);
        put(REGISTER_NAMES.get("PC"), (short) 0);
        put(REGISTER_NAMES.get("OF"), (short) 0);
        put(REGISTER_NAMES.get("RD"), (short) 0);
    }};

    private int[] parseInstructions(String fileName) {
        int[] result = new int[INSTRUCTION_MEM_SIZE];
        try {
            List<String> lines = Files.readAllLines(Paths.get(".", fileName));
            for(int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(" ");
                if(parts[0].equals("inc")) {
                    result[i] = (int)(COMMAND_NAMES.get(parts[0].toUpperCase())) << 16 |
                        (short)(((short)(REGISTER_NAMES.get(parts[1].toUpperCase()) << 8) | (byte) 0));
                } else if (parts[0].equals("jmple") || parts[0].equals("set")) {
                    result[i] = (int)(COMMAND_NAMES.get(parts[0].toUpperCase())) << 16 |
                            (short)((short)(REGISTER_NAMES.get(parts[1].toUpperCase()) << 8) | Byte.parseByte(parts[2]));
                } else {
                    result[i] = (int)(COMMAND_NAMES.get(parts[0].toUpperCase())) << 16 |
                            (short)(((short)(REGISTER_NAMES.get(parts[1].toUpperCase())) << 8) | REGISTER_NAMES.get(parts[2].toUpperCase()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    private short[] parseData(String fileName) {
        short[] result = new short[DATA_MEM_SIZE];
        try {
            List<String> lines = Files.readAllLines(Paths.get(".", fileName));
            for(int i = 0; i < lines.size(); i++) {
                result[i] = Short.parseShort(lines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    public void start(Program program) {
        start(program, false);
    }

    public void start(Program program, boolean fromFile) {
        if(!fromFile) {
            instructions = program.getInstructions();
            data = program.getData();
        } else {
            instructions = parseInstructions(program.getInstructionFileName());
            data = parseData(program.getDataFileName());
        }

        while(registers.get(REGISTER_NAMES.get("PC")) < INSTRUCTION_MEM_SIZE) {
            int instruction = instructions[registers.get(REGISTER_NAMES.get("PC"))];
            short command = (short) (instruction >> 16);
            byte op1 = (byte) (instruction >> 8);
            byte op2 = (byte) instruction;
            System.out.printf("Command: %s(%d) | op1 = %d | op2 = %d\n",
                    COMMANDS.get(command), command, op1, op2);
            switch (COMMANDS.get(command)) {
                case "NONE": break;
                case "MOVD":
                    registers.put(op2, data[registers.get(op1)]);
                    break;
                case "SET":
                    registers.put(op1, (short) op2);
                    break;
                case "ADD":
                    int sum = registers.get(op1) + registers.get(op2);
                    if(sum > Short.MAX_VALUE)
                        registers.put(REGISTER_NAMES.get("OF"), (short) 1);
                    registers.put(op1, (short)(sum));
                    break;
                case "INC":
                    int inc = registers.get(op1) + 1;
                    if(inc > Short.MAX_VALUE)
                        registers.put(REGISTER_NAMES.get("OF"), (short) 1);
                    registers.put(op1, (short)(inc));
                    break;
                case "JMPLE":
                    if(registers.get(REGISTER_NAMES.get("RCMP")) <= registers.get(op1)) {
                        registers.put(REGISTER_NAMES.get("PC"), (short) (op2 - 1) );
                    }
                    break;
                case "MOV":
                    registers.put(op1, registers.get(op2));
                    break;
                case "MUL":
                    int mul = registers.get(op1) * registers.get(op2);
                    if(mul > Short.MAX_VALUE)
                        registers.put(REGISTER_NAMES.get("OF"), (short) 1);
                    registers.put(op1, (short)(mul));
                    break;
            }
            registers.put(REGISTER_NAMES.get("PC"), (short)(registers.get(REGISTER_NAMES.get("PC")) + 1));
            System.out.printf("RA = %d | RB = %d | RC = %d | RI = %d | RCMP = %d | PC = %d | OF = %d%n | RD = %d\n",
                    registers.get(REGISTER_NAMES.get("RA")),
                    registers.get(REGISTER_NAMES.get("RB")),
                    registers.get(REGISTER_NAMES.get("RC")),
                    registers.get(REGISTER_NAMES.get("RI")),
                    registers.get(REGISTER_NAMES.get("RCMP")),
                    registers.get(REGISTER_NAMES.get("PC")),
                    registers.get(REGISTER_NAMES.get("OF")),
                    registers.get(REGISTER_NAMES.get("RD"))
            );
        }
    }
}
