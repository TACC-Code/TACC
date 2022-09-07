class BackupThread extends Thread {
                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeFloat(number, input.readFloat(), repeated);
                }
}
