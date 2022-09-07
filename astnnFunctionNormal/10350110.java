class BackupThread extends Thread {
    public Channel getChannelById(int id) {
        return root.getChannelByIdRecursive(id);
    }
}
