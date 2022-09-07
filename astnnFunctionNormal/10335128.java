class BackupThread extends Thread {
    Object getInputStreamOrErrorMessageFromName(String name) {
        String errorMessage = null;
        int iurlPrefix;
        for (iurlPrefix = urlPrefixes.length; --iurlPrefix >= 0; ) if (name.startsWith(urlPrefixes[iurlPrefix])) break;
        try {
            InputStream in;
            int length;
            if (appletDocumentBase == null) {
                if (iurlPrefix >= 0) {
                    URL url = new URL(name);
                    URLConnection conn = url.openConnection();
                    length = conn.getContentLength();
                    in = conn.getInputStream();
                } else {
                    File file = new File(name);
                    length = (int) file.length();
                    in = new FileInputStream(file);
                }
            } else {
                if (iurlPrefix >= 0 && appletProxy != null) name = appletProxy + "?url=" + URLEncoder.encode(name, "utf-8");
                URL url = new URL(appletDocumentBase, name);
                URLConnection conn = url.openConnection();
                length = conn.getContentLength();
                in = conn.getInputStream();
            }
            return new MonitorInputStream(in, length);
        } catch (Exception e) {
            errorMessage = "" + e;
        }
        return errorMessage;
    }
}
