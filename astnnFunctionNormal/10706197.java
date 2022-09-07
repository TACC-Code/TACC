class BackupThread extends Thread {
    public void generer(String input, String output) throws IOException {
        this.write(output, this.read(input));
    }
}
