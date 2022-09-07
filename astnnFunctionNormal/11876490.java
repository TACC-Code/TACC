class BackupThread extends Thread {
    private void unZip(String zipFilePath, File toUnzipFold) throws IOException {
        if (!toUnzipFold.exists()) {
            toUnzipFold.mkdirs();
        }
        ZipFile zfile = new ZipFile(zipFilePath);
        Enumeration zList = zfile.getEntries();
        byte[] buf = new byte[1024];
        File tmpfile = null;
        File tmpfold = null;
        while (zList.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) zList.nextElement();
            tmpfile = new File(toUnzipFold.getAbsolutePath() + File.separator + ze.getName());
            if (ze.isDirectory()) {
                continue;
            } else {
                tmpfold = tmpfile.getParentFile();
                if (!tmpfold.exists()) {
                    tmpfold.mkdirs();
                }
                OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpfile.getAbsolutePath()));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
        }
    }
}
