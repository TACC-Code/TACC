class BackupThread extends Thread {
    public static List<Copyright> getDefaultCopyrights() {
        URL url = Activator.getDefault().getBundle().getResource(REPOSITORY_FILENAME);
        try {
            return readCopyrights(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Copyright>();
        }
    }
}
