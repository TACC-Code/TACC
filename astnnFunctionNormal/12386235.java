class BackupThread extends Thread {
    public static InputStream getURLStream(URL url, int level) throws IOException {
        if (level > 5) {
            throw new IOException("Two many levels of redirection in URL");
        }
        URLConnection conn = url.openConnection();
        Map hdrs = conn.getHeaderFields();
        String[] keys = (String[]) hdrs.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i += 1) {
            String key = keys[i];
            if (key != null && key.toLowerCase().equals("location")) {
                String val = (String) ((List) hdrs.get(key)).get(0);
                if (val != null) {
                    val = val.trim();
                    if (val.length() > 0) {
                        return getURLStream(new URL(val), level + 1);
                    }
                }
            }
        }
        return conn.getInputStream();
    }
}
