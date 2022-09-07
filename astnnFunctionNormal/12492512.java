class BackupThread extends Thread {
    public static InputStream loadDocument(URL url, Proxy proxy) throws WebException {
        byte[] data = new byte[8192];
        URLConnection con = null;
        ByteArrayOutputStream bos = null;
        InputStream is = null;
        ByteArrayInputStream bis = null;
        try {
            try {
                if (proxy == null) con = url.openConnection(); else con = url.openConnection(proxy);
            } catch (IOException e) {
                throw new WebException("Failed to open URL " + url.toString());
            }
            try {
                is = con.getInputStream();
            } catch (IOException e) {
                throw new WebException("Failed to read from URL " + url.toString());
            }
            int i = 0;
            try {
                bos = new ByteArrayOutputStream();
                while ((i = is.read(data)) > 0) {
                    bos.write(data, 0, i);
                }
            } catch (IOException e) {
                throw new WebException("Failed to read data from URL " + url.toString());
            }
            bis = new ByteArrayInputStream(bos.toByteArray());
        } finally {
            try {
                if (bos != null) bos.close();
                if (is != null) is.close();
            } catch (IOException e) {
            }
        }
        return bis;
    }
}
