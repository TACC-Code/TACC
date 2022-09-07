class BackupThread extends Thread {
    public static Properties getProperties(URL url) throws IOException {
        InputStream l_in = null;
        Properties l_props = new Properties();
        l_in = url.openStream();
        l_props.load(l_in);
        if (l_in != null) {
            l_in.close();
        }
        return l_props;
    }
}
