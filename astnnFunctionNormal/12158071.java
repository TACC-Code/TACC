class BackupThread extends Thread {
    private static Properties load(URL url) {
        Properties props = new Properties();
        try {
            InputStream is = null;
            try {
                is = url.openStream();
                props.load(is);
            } finally {
                if (is != null) is.close();
            }
        } catch (IOException e) {
        }
        return props;
    }
}
