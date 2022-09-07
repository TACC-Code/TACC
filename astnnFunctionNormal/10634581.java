class BackupThread extends Thread {
    private void copyNoSubstitute() throws IOException {
        int read = inputStream.read();
        while (read != -1) {
            outputStream.write(read);
            read = inputStream.read();
        }
        System.out.println("copyNoSubsitute: close");
        outputStream.close();
        inputStream.close();
    }
}
