class BackupThread extends Thread {
    byte[] serveGeneratedFile(File zipOutFilename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fisZipped = new FileInputStream(zipOutFilename);
        byte[] bufferOut = new byte[4096];
        int readOut = 0;
        while ((readOut = fisZipped.read(bufferOut)) > 0) {
            baos.write(bufferOut, 0, readOut);
        }
        return baos.toByteArray();
    }
}
