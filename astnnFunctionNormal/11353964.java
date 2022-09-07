class BackupThread extends Thread {
        public void run() {
            try {
                byte[] buf = new byte[BUF];
                int read = 0;
                while (!isInterrupted() && (read = _is.read(buf)) != -1) {
                    if (read == 0) continue;
                    _os.write(buf, 0, read);
                    _os.flush();
                }
            } catch (InterruptedIOException iioe) {
            } catch (Throwable t) {
                _thrownError = t;
            } finally {
                try {
                    if (_closeInput) {
                        _is.close();
                    } else {
                        _os.close();
                    }
                } catch (IOException ioe) {
                }
            }
            try {
                _barrier.await();
            } catch (InterruptedException ie) {
            } catch (BrokenBarrierException bbe) {
            }
        }
}
