class BackupThread extends Thread {
    public static void write(File file, InputStream input, boolean append) throws IOException {
        makedirs(file);
        BufferedOutputStream output = null;
        try {
            int contentLength = input.available();
            output = new BufferedOutputStream(new FileOutputStream(file, append));
            while (contentLength-- > 0) {
                output.write(input.read());
            }
        } finally {
            close(input);
            close(output);
        }
    }
}
