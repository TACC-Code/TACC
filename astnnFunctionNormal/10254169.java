class BackupThread extends Thread {
    public void upZipFile(String zipFile, boolean decrypt, String specifiedDir) throws Exception {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[BUFFER];
        byte[] encrypByte = new byte[encrypLength];
        int readLen = 0;
        logger.error("解压文件：" + zipFile);
        long[] sDCardRealease = LoadResources.readSDCard();
        long[] extSDCardRealease = LoadResources.readExtSDCard();
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (stopZipFile) {
                break;
            }
            if (specifiedDir != null && !"..".equals(specifiedDir) && ze.getName().indexOf(specifiedDir) < 0) continue;
            if (ze.isDirectory()) {
                File f = new File(sdPath + ze.getName());
                f.mkdir();
                continue;
            }
            File tempFile = null;
            RandomAccessFile os = null;
            try {
                if (sDCardRealease[1] >= ze.getSize()) {
                    tempFile = getRealFileName(sdPath, ze.getName(), specifiedDir);
                    os = new RandomAccessFile(tempFile.getAbsoluteFile(), "rw");
                    sDCardRealease[1] = sDCardRealease[1] - ze.getSize();
                } else if (extSDCardRealease[1] >= ze.getSize()) {
                    tempFile = getRealFileName(extSdPath, ze.getName(), specifiedDir);
                    os = new RandomAccessFile(tempFile.getAbsoluteFile(), "rw");
                    extSDCardRealease[1] = extSDCardRealease[1] - ze.getSize();
                } else {
                    throw new IOException("空间不足");
                }
                logger.error("解压文件：" + ze.getName());
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                readLen = is.read(buf, 0, BUFFER);
                if (decrypt) {
                    System.arraycopy(buf, 0, encrypByte, 0, encrypLength);
                    byte[] temp = CryptionControl.getInstance().decryptECB(encrypByte, rootKey);
                    System.arraycopy(temp, 0, buf, 0, encrypLength);
                }
                while (readLen != -1) {
                    os.write(buf, 0, readLen);
                    readLen = is.read(buf, 0, BUFFER);
                }
                is.close();
                os.close();
            } catch (IOException e) {
                throw new IOException("解压失败");
            }
        }
        zfile.close();
    }
}
