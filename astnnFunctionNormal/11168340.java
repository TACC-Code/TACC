class BackupThread extends Thread {
    public static void convertEncoding(File textFile, String readEncoding, String writeEncoding) {
        String text = Streams.readAndClose(Streams.fileInr(textFile, readEncoding));
        Streams.writeAndClose(Streams.fileOutw(textFile, writeEncoding), text);
    }
}
