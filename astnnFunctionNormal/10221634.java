class BackupThread extends Thread {
    private void printQhullErrors(Process proc) throws IOException {
        boolean wrote = false;
        InputStream es = proc.getErrorStream();
        while (es.available() > 0) {
            System.out.write(es.read());
            wrote = true;
        }
        if (wrote) {
            System.out.println("");
        }
    }
}
