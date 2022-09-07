class BackupThread extends Thread {
    @Override
    protected InputStream[] getChannelsInputStreams() throws IOException {
        InputStream[] ret = new InputStream[channelsConfigLocations.length];
        int i = 0;
        for (String channelConfigLocation : channelsConfigLocations) {
            Resource resource = applicationContext.getResource(channelConfigLocation);
            ret[i] = resource.getInputStream();
            i++;
        }
        return ret;
    }
}
