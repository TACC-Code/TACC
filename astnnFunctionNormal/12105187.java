class BackupThread extends Thread {
    protected void loadImage() {
        java.net.URL urlImage = null;
        StringBuffer sb = new StringBuffer(this.sUrl);
        if (this.sIdImage != null) {
            sb.append("/servlet/ShowImage?id=").append(this.sIdImage);
        } else if (this.sHashImage != null) {
            sb.append("/servlet/ShowImage?hash=").append(this.sHashImage);
        }
        sb.append("&w=").append(this.getWidth()).append("&h=").append(this.getHeight());
        try {
            urlImage = new java.net.URL(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (urlImage != null) {
            java.net.HttpURLConnection huc = null;
            InputStream inS = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1000];
            int len = 0;
            try {
                huc = (java.net.HttpURLConnection) urlImage.openConnection();
                huc.setRequestProperty("Cookie", "JSESSIONID=" + this.sJsessionId);
                inS = huc.getInputStream();
                while ((len = inS.read(buffer)) >= 0) {
                    baos.write(buffer, 0, len);
                }
                inS.close();
                java.awt.Image image = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
                this.iiImage = new ImageIcon(image);
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
        if (iiImage != null) {
            java.awt.Image img = null;
            img = this.iiImage.getImage();
            if ((this.getHeight() * img.getWidth(this)) < (this.getWidth() * img.getHeight(this))) {
                this.iiImage = new ImageIcon(img.getScaledInstance(-1, this.getHeight(), java.awt.Image.SCALE_SMOOTH));
            } else {
                this.iiImage = new ImageIcon(img.getScaledInstance(this.getWidth(), -1, java.awt.Image.SCALE_SMOOTH));
            }
        }
    }
}
