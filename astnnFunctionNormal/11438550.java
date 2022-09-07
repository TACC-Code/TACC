class BackupThread extends Thread {
    public void showURL(URL url, String title) {
        try {
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            char[] buf = new char[8192];
            StringWriter sw = new StringWriter();
            int bytesRead = isr.read(buf);
            while (bytesRead > 0) {
                sw.write(buf, 0, bytesRead);
                bytesRead = isr.read(buf);
            }
            isr.close();
            String s = sw.toString();
            s = s.replaceAll("<[^>]+>", "");
            s = s.trim();
            statusLog.writeln(s);
        } catch (IOException ioe) {
            statusLog.writeln("Error reading URL in showURL(): " + ioe.getMessage());
        }
    }
}
