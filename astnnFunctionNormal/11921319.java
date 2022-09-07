class BackupThread extends Thread {
    private boolean download(URL target, File file) {
        _log.addDebug("JarDiffHandler:  Doing download");
        boolean ret = true;
        boolean delete = false;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(target.openStream());
            out = new BufferedOutputStream(new FileOutputStream(file));
            int read = 0;
            int totalRead = 0;
            byte[] buf = new byte[BUF_SIZE];
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
                totalRead += read;
            }
            _log.addDebug("total read: " + totalRead);
            _log.addDebug("Wrote URL " + target.toString() + " to file " + file);
        } catch (IOException ioe) {
            _log.addDebug("Got exception while downloading resource: " + ioe);
            ret = false;
            if (file != null) delete = true;
        } finally {
            try {
                in.close();
                in = null;
            } catch (IOException ioe) {
                _log.addDebug("Got exception while downloading resource: " + ioe);
            }
            try {
                out.close();
                out = null;
            } catch (IOException ioe) {
                _log.addDebug("Got exception while downloading resource: " + ioe);
            }
            if (delete) {
                file.delete();
            }
        }
        return ret;
    }
}
