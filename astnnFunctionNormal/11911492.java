class BackupThread extends Thread {
    public static void write(File file, Reader reader, boolean append) throws IOException {
        makedirs(file);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, append));
            int i = -1;
            while ((i = reader.read()) != -1) {
                writer.write(i);
            }
        } finally {
            close(reader);
            close(writer);
        }
    }
}
