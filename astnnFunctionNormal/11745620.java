class BackupThread extends Thread {
    @Override
    public void onDisconnect() {
        String[] botChannels;
        botChannels = getChannels();
        for (String botChannel : botChannels) {
            sendMessage(botChannel, Colors.RED + "* Disabling LVM Plugin...");
        }
    }
}
