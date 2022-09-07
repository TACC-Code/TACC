class BackupThread extends Thread {
    public Channel(String chName) {
        try {
            getChannelByName(chName);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }
}
