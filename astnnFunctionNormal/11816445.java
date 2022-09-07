class BackupThread extends Thread {
    public static void copyBytes(InputStream input, OutputStream output) throws Exception {
        StreamReader reader = new StreamReader(input);
        while (!reader.isEof()) output.write(reader.readBytes(1000));
    }
}
