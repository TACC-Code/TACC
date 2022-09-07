class BackupThread extends Thread {
        public void start() throws IOException {
            if (!trns.isConnected()) {
                trns.connect();
                rcv.setChannel(trns.getChannel());
            }
            rcv.startListening();
        }
}
