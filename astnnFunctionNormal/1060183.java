class BackupThread extends Thread {
    private boolean checkConnection(String[] name) {
        for (int i = 0; i < name.length; i++) {
            Channel tempChannel = ChannelFactory.defaultFactory().getChannel(name[i]);
            try {
                tempChannel.checkConnection();
            } catch (ConnectionException e) {
                return false;
            }
        }
        return true;
    }
}
