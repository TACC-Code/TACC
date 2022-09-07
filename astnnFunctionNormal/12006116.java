class BackupThread extends Thread {
    public String copyUrl(String sUrl, String sFileName) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(sUrl);
            InputStream is = url.openStream();
            FileOutputStream fos = null;
            fos = new FileOutputStream("c:\\temp\\mf\\" + sFileName);
            int oneChar, count = 0;
            while ((oneChar = is.read()) != -1) {
                fos.write(oneChar);
                sb.append((char) oneChar);
                count++;
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            Logger.error(e);
        }
        return sb.toString();
    }
}
