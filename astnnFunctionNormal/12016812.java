class BackupThread extends Thread {
    private CacheManager() {
        String enabled = System.getProperty(EPROPERTIES_CACHE, "true");
        if (enabled.equals("true")) {
            if (System.getProperty(CACHE_ROOT_PROPERTY) != null) {
                cacheRoot = new File(System.getProperty(CACHE_ROOT_PROPERTY));
            } else {
                cacheRoot = new File(System.getProperty("user.home") + "/.eproperties/cache/");
            }
            log.debug("EProperties: CacheManager root at " + cacheRoot);
            if (!cacheRoot.exists()) {
                boolean success = cacheRoot.mkdirs();
                if (!success) {
                    online = false;
                    state = "Cannot create cache root dir at " + cacheRoot.getAbsolutePath();
                    log.error(state);
                    log.error("CacheManager will be unavilable for read/write property caching.");
                }
            }
            if (!cacheRoot.canWrite()) {
                online = false;
                state = "Cannot write in cache dir.  CacheManager unavailable.";
                log.error(state);
                log.error("CacheManager will be unavilable for read/write property caching.");
            }
        } else {
            online = false;
            state = "Disabled by system property (eproperties.cache != true)";
        }
        log.debug("EProperties: CacheManager state: " + state);
    }
}
