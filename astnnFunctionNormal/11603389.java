class BackupThread extends Thread {
    @Override
    public void cleanup() {
        getLogger().log(Level.ALL, "end packet send (over the wire) from " + getOperatorName() + " to " + _remoteIP + ":" + _remorePort);
        try {
            _outStream.flush();
            _outStream.close();
            _socket.getChannel().close();
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
