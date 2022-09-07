class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public void loadPropertiesFromClass(Properties props, Class<?> clazz) {
        Map<Class<?>, Properties> cache = (Map<Class<?>, Properties>) getAttribute(CONSUMER_PROPERTIES_CACHE);
        if (cache == null) {
            cache = new HashMap<Class<?>, Properties>(7);
            setAttribute(CONSUMER_PROPERTIES_CACHE, cache);
        }
        Properties defaultProps = cache.get(clazz);
        if (defaultProps == null) {
            defaultProps = new Properties();
            cache.put(clazz, defaultProps);
            String resource = clazz.getName().replace('.', '/') + ".properties";
            URL url = getResource(resource);
            if (url != null) {
                try {
                    defaultProps.load(url.openStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (defaultProps.size() != 0) props.putAll(defaultProps);
    }
}
