class BackupThread extends Thread {
                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeInt32(number, input.readInt32(), repeated);
                }
}
