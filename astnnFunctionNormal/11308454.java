class BackupThread extends Thread {
    private Reader openResource(String url) {
        if (m_cp.getM_verbose()) System.out.println("Trying to open resource url '" + url + "'...");
        ClassLoader clzldr = this.getClass().getClassLoader();
        if (clzldr instanceof java.net.URLClassLoader) {
            java.net.URLClassLoader urlclzldr = (java.net.URLClassLoader) clzldr;
            if (m_cp.getM_verbose()) {
                URL urls[] = urlclzldr.getURLs();
                System.out.println("Searching in " + urls.length + " URLs:");
                for (int i = 0; i < urls.length; i++) System.out.println(" -> " + urls[i]);
            }
            URL jarurl = null;
            try {
                jarurl = urlclzldr.findResource(url);
                if (jarurl != null) {
                    java.net.URLConnection conn = jarurl.openConnection();
                    conn.connect();
                    if (m_cp.getM_verbose()) System.out.println("Successfully opened resource url '" + url + "'.");
                    return new InputStreamReader(conn.getInputStream());
                }
            } catch (IOException ex) {
            }
        }
        if (m_cp.getM_verbose()) System.out.println("Failed to open resource url '" + url + "'.");
        return null;
    }
}
