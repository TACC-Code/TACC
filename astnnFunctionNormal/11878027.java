class BackupThread extends Thread {
    public ImageIcon getImageIcon() {
        if (imageIcon == null) {
            try {
                logger.fine("Creating icon for resource '" + name + "'");
                long startTime = System.currentTimeMillis();
                if (name.startsWith("http:") || name.startsWith("file:")) {
                    URL url = new URL(name);
                    if (protocolInfo.indexOf("image/bmp") >= 0) {
                        imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(BMPReader.getBMPImage(url.openStream())));
                    } else {
                        imageIcon = new ImageIcon(url);
                    }
                } else {
                    imageIcon = new ImageIcon(name);
                }
                logger.fine("Icon creation time (ms): " + (System.currentTimeMillis() - startTime));
            } catch (MalformedURLException e) {
                logger.warning("Exception " + e);
                return null;
            } catch (IOException e) {
                logger.warning("Exception " + e);
                return null;
            }
        }
        return imageIcon;
    }
}
