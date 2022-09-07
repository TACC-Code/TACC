class BackupThread extends Thread {
    @Override
    public void prepare() {
        try {
            _socket = OhuaSocketFactory.getInstance().createSocket(_remoteIP, _remorePort);
            _socket.getChannel().configureBlocking(false);
            _socket.setOOBInline(true);
            setChannel(_socket.getChannel());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _outStream = getOutputStream();
    }
}
