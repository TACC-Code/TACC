class BackupThread extends Thread {
    public static Properties loadProperties(File file) throws IOException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            lockIfPossible(fin.getChannel(), true);
            Properties props = new Properties();
            props.load(fin);
            return props;
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
