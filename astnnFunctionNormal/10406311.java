class BackupThread extends Thread {
    public static synchronized Map getConnectionMap(String fileName) {
        if (propertyMap != null) {
            return propertyMap;
        }
        InputStream is = null;
        try {
            Properties props = new Properties();
            URL url = getRelativePath(fileName);
            props = new Properties();
            is = url.openStream();
            props.load(is);
            Enumeration keys = props.keys();
            propertyMap = new HashMap();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                Object value = props.get(key);
                propertyMap.put(key.toLowerCase(), value);
            }
            return propertyMap;
        } catch (FileNotFoundException e) {
            IllegalArgumentException ille = new IllegalArgumentException("cannot load: " + fileName + " " + e.getMessage());
            ille.setStackTrace(e.getStackTrace());
            throw ille;
        } catch (Exception e) {
            RuntimeException rte = new RuntimeException("cannot load: " + fileName + " " + e.getMessage());
            rte.setStackTrace(e.getStackTrace());
            throw rte;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException("cannot close: " + fileName + e.getMessage());
                }
            }
        }
    }
}
