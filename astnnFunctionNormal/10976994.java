class BackupThread extends Thread {
    public static void exportChannelList(String fileLocation) throws IOException {
        ImportExport.writeChannelsToFile(new LinkedList<Channel>(channels.getChannels()), fileLocation);
    }
}
