class BackupThread extends Thread {
    private void autojoinChannels() {
        xAutojoinChannels channels = new xAutojoinChannels(config.get(xConfig.CONFIG_JDBCURL));
        while (channels.hasResults()) this.joinChannel(channels.getChannel());
    }
}
