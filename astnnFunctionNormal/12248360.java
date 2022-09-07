class BackupThread extends Thread {
        @Override
        public void send(Buffer data) throws Exception {
            getLock().lock();
            try {
                disableSendErrHandler();
                getChannelState().send(this, data);
                enableSendErrHandler();
            } finally {
                getLock().unlock();
            }
        }
}
