class BackupThread extends Thread {
    public static int getLength(SiteInfoBean site) throws Exception {
        String protocol = site.getUrl().toLowerCase();
        if (protocol.startsWith("http")) {
            HttpURLConnection conn = null;
            for (int i = 0; i < 99; i++) {
                try {
                    URL url = new URL(site.getUrl());
                    conn = (HttpURLConnection) url.openConnection();
                    Iterator<String> itor = site.getHeaders().keySet().iterator();
                    while (itor.hasNext()) {
                        String key = itor.next();
                        conn.setRequestProperty(key, site.getHeaders().get(key));
                    }
                    conn.getInputStream();
                    String fileSizeStr = conn.getHeaderField("Content-Length");
                    String maxThreads = conn.getHeaderField("MaxThreads");
                    System.out.println("Max threads=" + maxThreads);
                    conn.disconnect();
                    System.out.println("�ļ��ܳ���=" + fileSizeStr);
                    return Integer.valueOf(fileSizeStr);
                } catch (Exception ex) {
                } finally {
                    if (conn != null) conn.disconnect();
                }
            }
        }
        return 0;
    }
}
