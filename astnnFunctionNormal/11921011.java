class BackupThread extends Thread {
    private void finishInput(BufferedReader input, PrintStream output) throws IOException {
        int read;
        while ((read = input.read()) != -1) {
            output.write(read);
        }
    }
}
