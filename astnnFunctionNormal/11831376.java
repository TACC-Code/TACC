class BackupThread extends Thread {
    public boolean connect() {
        try {
            URL url = createURL();
            if (url != null) {
                connection = url.openConnection();
                connection.connect();
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, properties.getProperty("connection.not.established"));
        }
        return false;
    }
}
