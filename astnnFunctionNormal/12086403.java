class BackupThread extends Thread {
    public static Image getRemoteImage(String url) {
        try {
            Image remote = new Image(Display.getCurrent(), NetUtil.createSafeURL(url).openStream());
            return resize(remote, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
