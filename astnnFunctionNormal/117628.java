class BackupThread extends Thread {
        void drain(InputStream is, OutputStream os) {
            try {
                while (is.available() > 0) os.write(is.read());
            } catch (java.io.IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
}
