class BackupThread extends Thread {
    public static void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
        if (ENUMS_BY_NAME) input.transferByteRangeTo(output, true, number, repeated); else output.writeEnum(number, input.readEnum(), repeated);
    }
}
