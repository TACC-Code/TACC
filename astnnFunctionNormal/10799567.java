class BackupThread extends Thread {
    public Map<String, Object> loadMap(URL url) throws IOException {
        if (!isSupported(url)) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = url.openStream();
        Properties bundle = new Properties();
        bundle.load(is);
        Enumeration e = bundle.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            Object value = bundle.get(key);
            if (value != null) {
                result.put(key, value);
            }
        }
        return result;
    }
}
