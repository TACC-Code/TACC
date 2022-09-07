class BackupThread extends Thread {
    public void loadURL(URL url) {
        try {
            load(url.openStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "I/O Error: " + e.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
