class BackupThread extends Thread {
        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = is.read(buffer);
                    if (read < 0) {
                        synchronized (WriteLock) {
                            buffer = ":RET=EOF".getBytes();
                            baos.write(buffer);
                        }
                        synchronized (ReadLock) {
                            ReadLock.notifyAll();
                        }
                        return;
                    }
                    if (read > 0) {
                        synchronized (WriteLock) {
                            baos.write(buffer, 0, read);
                        }
                        synchronized (ReadLock) {
                            ReadLock.notifyAll();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
}
