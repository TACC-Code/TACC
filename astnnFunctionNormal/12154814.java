class BackupThread extends Thread {
    protected String buildListUrl(final Channel channel, final int day) {
        String dayString = String.valueOf(day);
        String pageUrl = StringUtils.replaceEach(getTvgidsurl() + getListingQuery(), new String[] { "{day}", "{channel}" }, new String[] { dayString, channel.getChannelId() });
        return pageUrl;
    }
}
