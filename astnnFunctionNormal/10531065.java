class BackupThread extends Thread {
    private boolean getUrlInfo(boolean toread, boolean towrite) {
        if (toread == (input != null) && towrite == (output != null)) return true;
        try {
            URLConnection c = ((URL) source).openConnection();
            lastModified = new Date(c.getLastModified());
            size = c.getContentLength();
            if (mimetype == null) mimetype = c.getContentType();
            if (toread) {
                InputStream i = c.getInputStream();
                if ((i != null) == (input != null)) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    }
                }
                input = i;
            }
            if (towrite) {
                OutputStream o = c.getOutputStream();
                if ((o != null) == (output != null)) {
                    try {
                        output.close();
                    } catch (Exception e) {
                    }
                }
                output = o;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
