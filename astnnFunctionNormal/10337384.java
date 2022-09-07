class BackupThread extends Thread {
    protected void getFile(ZipEntry e) throws IOException {
        String zipName = e.getName();
        switch(mode) {
            case EXTRACT:
                if (zipName.startsWith("/")) {
                    if (!warnedMkDir) {
                        logger.warn("Ignoring absolute paths");
                    }
                    warnedMkDir = true;
                    zipName = zipName.substring(1);
                }
                if (zipName.endsWith("/")) {
                    return;
                }
                int ix = zipName.lastIndexOf('/');
                if (ix > 0) {
                    String dirName = zipName.substring(0, ix);
                    if (!dirsMade.contains(dirName)) {
                        File d = new File(dirName);
                        if (!(d.exists() && d.isDirectory())) {
                            logger.debug("Creating Directory: " + dirName);
                            if (!d.mkdirs()) {
                                logger.warn("Warning: unable to mkdir " + dirName);
                            }
                            dirsMade.add(dirName);
                        }
                    }
                }
                logger.debug("Creating " + zipName);
                FileOutputStream os = new FileOutputStream(zipName);
                InputStream is = zippy.getInputStream(e);
                int n = 0;
                while ((n = is.read(b)) > 0) os.write(b, 0, n);
                is.close();
                os.close();
                break;
            case LIST:
                if (e.isDirectory()) {
                    logger.debug("Directory " + zipName);
                } else {
                    logger.debug("File " + zipName);
                }
                break;
            default:
                throw new IllegalStateException("mode value (" + mode + ") bad");
        }
    }
}
