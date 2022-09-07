class BackupThread extends Thread {
    public byte[] getPdfBuffer() {
        URL url = null;
        byte[] pdfByteArray = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            url = new URL("file:" + fileName);
            is = url.openStream();
            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            pdfByteArray = os.toByteArray();
            is.close();
            os.close();
        } catch (IOException e) {
            LogWriter.writeLog("[PDF] Exception " + e + " getting byte[] for " + fileName);
        }
        return pdfByteArray;
    }
}
