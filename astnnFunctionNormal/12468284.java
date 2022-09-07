class BackupThread extends Thread {
    public Object getChannelObject() throws XAwareException {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(getFactoryClassName());
        basicDataSource.setUrl(getUrl());
        basicDataSource.setUsername(getUsername());
        basicDataSource.setPassword(getPassword());
        final Properties connProps = getConnectionProperties();
        final Enumeration keysEnum = connProps.keys();
        while (keysEnum.hasMoreElements()) {
            final String key = (String) keysEnum.nextElement();
            final String value = connProps.getProperty(key);
            basicDataSource.addConnectionProperty(key, value);
        }
        return basicDataSource;
    }
}
