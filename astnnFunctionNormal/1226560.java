class BackupThread extends Thread {
    private Image getImage(String url) {
        Image img = null;
        try {
            img = new Image(getSite().getShell().getDisplay(), new URL(url).openStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
