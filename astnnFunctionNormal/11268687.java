class BackupThread extends Thread {
    public Channel(Integer chID) {
        try {
            getChannelByID(chID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
