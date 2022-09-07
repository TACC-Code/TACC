class BackupThread extends Thread {
                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeFixed64(number, input.readFixed64(), repeated);
                }
}
