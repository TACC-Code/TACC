class BackupThread extends Thread {
    public static StringBuffer readSBuffer(URL url) throws IOException {
        InputStream fis = null;
        StringBuffer sb = new StringBuffer();
        try {
            fis = url.openStream();
            byte[] buffer = new byte[8 * 21000];
            int bytes = 0;
            while ((bytes = fis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytes));
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
                fis = null;
            }
        }
        return sb;
    }
}
