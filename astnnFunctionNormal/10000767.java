class BackupThread extends Thread {
        private void initialize() throws SchedulerException {
            InputStream f = null;
            try {
                String furl = null;
                File file = new File(getFileName());
                if (!file.exists()) {
                    URL url = classLoadHelper.getResource(getFileName());
                    if (url != null) {
                        furl = URLDecoder.decode(url.getPath());
                        file = new File(furl);
                        try {
                            f = url.openStream();
                        } catch (IOException ignor) {
                        }
                    }
                } else {
                    try {
                        f = new java.io.FileInputStream(file);
                    } catch (FileNotFoundException e) {
                    }
                }
                if (f == null) {
                    if (isFailOnFileNotFound()) {
                        throw new SchedulerException("File named '" + getFileName() + "' does not exist.");
                    } else {
                        getLog().warn("File named '" + getFileName() + "' does not exist.");
                    }
                } else {
                    fileFound = true;
                    filePath = (furl != null) ? furl : file.getAbsolutePath();
                    fileBasename = file.getName();
                }
            } finally {
                try {
                    if (f != null) {
                        f.close();
                    }
                } catch (IOException ioe) {
                    getLog().warn("Error closing jobs file " + getFileName(), ioe);
                }
            }
        }
}
