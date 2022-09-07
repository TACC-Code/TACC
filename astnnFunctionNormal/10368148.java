class BackupThread extends Thread {
    public void run() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (isConnectionRunning) {
            try {
                while (inputStream.available() > 0) {
                    baos.write(inputStream.read());
                }
                if (null != socketListener) {
                    byte[] received = baos.toByteArray();
                    if (null != received) {
                        socketListener.messageReceived(received);
                        received = null;
                        baos.reset();
                    }
                }
                Thread.sleep(100);
            } catch (IOException ioe) {
                if (isConnectionRunning) {
                    logger.log(Level.SEVERE, "IOException reading from inputStream.");
                    tcpConnection.errorOccuredCallback();
                }
            } catch (NullPointerException npe) {
                if (isConnectionRunning) {
                    logger.log(Level.SEVERE, "NPException from inputStream.");
                    tcpConnection.errorOccuredCallback();
                }
            } catch (InterruptedException ie) {
            }
        }
        try {
            baos.close();
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "IOException closing baos.");
        } finally {
            baos = null;
        }
    }
}
