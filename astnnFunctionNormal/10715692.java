class BackupThread extends Thread {
    public String getImage(JProgressBar bar) {
        long size = 0;
        try {
            size = photo.getSize();
            bar.setMaximum((int) size);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bar.setValue(0);
        File image = new File("TMP/W_" + photo.getTitle().getPlainText());
        try {
            if (!image.exists()) {
                image.deleteOnExit();
                URL url = null;
                BufferedOutputStream fOut = null;
                try {
                    url = new URL(photo.getMediaContents().get(0).getUrl());
                    InputStream html = null;
                    html = url.openStream();
                    fOut = new BufferedOutputStream(new FileOutputStream(image));
                    byte[] buffer = new byte[32 * 1024];
                    int bytesRead = 0;
                    int in = 0;
                    while ((bytesRead = html.read(buffer)) != -1) {
                        in += bytesRead;
                        bar.setValue(in);
                        fOut.write(buffer, 0, bytesRead);
                    }
                    html.close();
                    fOut.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "<html><body bgcolor=\"rgb(240, 240, 240)\"><img src=\"file:\\" + image.getAbsolutePath() + "\" width=" + width + " height=" + this.hight + "></img></body></html>";
    }
}
