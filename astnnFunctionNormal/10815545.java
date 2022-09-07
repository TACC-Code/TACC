class BackupThread extends Thread {
    public int read() throws IOException {
        int result;
        while (pipeIn.available() == 0 && !eof) {
            int nbread = streamToZip.read(array);
            if (nbread == -1) {
                eof = true;
                zos.closeEntry();
            } else {
                zos.write(array, 0, nbread);
            }
        }
        if (eof && pipeIn.available() == 0) {
            result = -1;
        } else {
            result = pipeIn.read();
        }
        return result;
    }
}
