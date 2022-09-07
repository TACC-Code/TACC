class BackupThread extends Thread {
    public void addHandler(String handlerId, Configuration config) throws ConfigurationException {
        if (config == null) {
            throw new NullPointerException("Configuration is null");
        }
        if (this.getChannelBuilder() == null) {
            throw new NullPointerException("ChannelBuilder has not been set!");
        }
        String titleKey = KEY_PREFIX + handlerId + ".title";
        String urlKey = KEY_PREFIX + handlerId + ".url";
        String descKey = KEY_PREFIX + handlerId + ".description";
        String patternKey = KEY_PREFIX + handlerId + ".pattern";
        String orderKey = KEY_PREFIX + handlerId + ".order";
        String dateKey = KEY_PREFIX + handlerId + ".date";
        String generator = config.getString("any2rss.generator", "Any2Rss");
        String title = config.getString(titleKey, "Untitled");
        String siteUrl = config.getString(urlKey, "http://nourl.org");
        String desc = config.getString(descKey, "No Descriptions");
        String datePatternStr = config.getString(dateKey);
        String itemPatternStr = null;
        String[] itemPatternOrderStr = null;
        try {
            itemPatternStr = config.getString(patternKey);
            itemPatternOrderStr = config.getStringArray(orderKey);
        } catch (NoSuchElementException ne) {
            throw new ConfigurationException("Missing required configurations.", ne);
        }
        log.info("generator=" + generator + ", " + titleKey + "=" + title + ", " + urlKey + "=" + siteUrl + ", " + descKey + "=" + desc + ", " + patternKey + "=" + itemPatternStr + ", " + orderKey + "=" + itemPatternOrderStr + ", " + dateKey + "=" + datePatternStr);
        PatternContentHandler newPattern = new PatternContentHandler();
        newPattern.setChannelBuilder(this.getChannelBuilder());
        newPattern.configure(generator, title, siteUrl, desc, itemPatternStr, itemPatternOrderStr, datePatternStr);
        handlers.put(handlerId, newPattern);
    }
}
