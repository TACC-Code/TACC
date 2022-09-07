class BackupThread extends Thread {
                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeBool(number, input.readBool(), repeated);
                }
}
