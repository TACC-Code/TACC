class BackupThread extends Thread {
    private void copyPrefsFile(final StringSetting setting, final LongSetting last_modified) {
        String filename = setting.getValue().trim();
        if (!filename.startsWith("etc")) {
            setting.revertToDefault();
            filename = setting.getValue().trim();
        }
        int idx = filename.lastIndexOf(java.io.File.separator);
        if (idx == -1) {
            LOG.debug("idx == -1");
            idx = filename.lastIndexOf("/");
        }
        String trimmedFile = filename.substring(idx, filename.length());
        java.io.File fileHandle = new java.io.File(CommonUtils.getUserSettingsDir().getAbsolutePath() + trimmedFile);
        try {
            Thread a = Thread.currentThread();
            if (!a.isAlive()) {
                LOG.debug("Thread is not alive!");
            }
            ClassLoader c = a.getContextClassLoader();
            URL filePath = c.getResource(filename);
            if (filePath == null) {
                LOG.debug("[NULL]trust.keystore file path: " + filePath.toString());
                return;
            }
            URLConnection url_connect = filePath.openConnection();
            if (!fileHandle.exists()) {
                LOG.debug("no handles!!!");
            }
            if (!fileHandle.exists() || (url_connect.getLastModified() != last_modified.getValue())) {
                LOG.debug("trying to copy from: " + filePath);
                LOG.debug("trying to copy to  : " + fileHandle);
                FileOutputStream out = new FileOutputStream(fileHandle);
                java.io.InputStream in = url_connect.getInputStream();
                byte[] bytes = new byte[4096];
                int read = -1;
                do {
                    read = in.read(bytes);
                    if (read != -1) {
                        out.write(bytes, 0, read);
                    }
                } while (read != -1);
                out.flush();
                out.close();
                in.close();
            }
            last_modified.setValue(url_connect.getLastModified());
            setting.setValue(fileHandle.getAbsolutePath());
            LionShareApplicationSettings.instance().getFactory().save();
        } catch (Exception e) {
            LOG.debug("Reasons for errors 1 : " + e.getMessage());
            LOG.debug("Reasons for errors 2 : " + e.getLocalizedMessage());
            LOG.debug("Reasons for errors 3 : " + e.toString());
            LOG.trace("Unable to copy ", e);
        }
    }
}
