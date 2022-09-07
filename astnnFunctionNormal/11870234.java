class BackupThread extends Thread {
    public Image getImage(java.net.URL url) throws java.io.IOException {
        URLConnection u = url.openConnection();
        DataInputStream dis = new DataInputStream(u.getInputStream());
        BufferedInputStream in = new BufferedInputStream(dis);
        int[] pixels = getImage(in);
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels, 0, width));
    }
}
