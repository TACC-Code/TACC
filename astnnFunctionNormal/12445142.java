class BackupThread extends Thread {
    public static final EncodedImage loadEncodedImg(String url) {
        byte[] imgBytes = null;
        InputStream ins = null;
        try {
            ins = Connector.openInputStream(url);
            ByteArrayOutputStream ous = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int n; (n = ins.read(buf)) != -1; ) ous.write(buf, 0, n);
            buf = null;
            imgBytes = ous.toByteArray();
            IoUtil.closeQuiet(ous);
            ous = null;
        } catch (Exception ex) {
            ;
        } finally {
            IoUtil.closeQuiet(ins);
            ins = null;
        }
        EncodedImage result = (imgBytes != null) ? EncodedImage.createEncodedImage(imgBytes, 0, imgBytes.length) : null;
        return result;
    }
}
