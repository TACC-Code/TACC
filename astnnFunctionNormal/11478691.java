class BackupThread extends Thread {
    private static void copyFile(InputStream in, String outFile) throws IOException {
        FileOutputStream outputFile = new FileOutputStream(outFile);
        byte buffer[] = new byte[1024];
        while (true) {
            int readBytes = in.read(buffer);
            if (readBytes < 0) break;
            outputFile.write(buffer, 0, readBytes);
        }
        outputFile.flush();
        outputFile.close();
    }
}
