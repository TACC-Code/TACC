class BackupThread extends Thread {
    public boolean checksum(File file, Package myPackage) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            final FileInputStream in = new FileInputStream(file);
            int len;
            final byte[] buf = new byte[4096];
            while ((len = in.read(buf)) > 0) md.update(buf, 0, len);
            in.close();
            final byte[] fileMD5sum = md.digest();
            md.reset();
            if (myPackage.getMD5sum().equalsIgnoreCase(byteArrayToHexString(fileMD5sum))) {
                File parentFile = file.getParentFile();
                if (parentFile != null && parentFile.exists()) parentFile = parentFile.getParentFile();
                final String testFileName = parentFile.getPath() + File.separator + file.getName();
                if (!file.renameTo(new File(testFileName))) {
                    String errorMessage = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DownloadFiles.checksum.md5different", "No entry found for DownloadFiles.checksum.md5different");
                    logger.error(errorMessage);
                    pkgmgr.addWarning(errorMessage);
                    return false;
                }
            } else {
                String errorMessage = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DownloadFiles.checksum.md5different", "No entry found for DownloadFiles.checksum.md5different");
                logger.error(errorMessage);
                pkgmgr.addWarning(errorMessage);
                file.delete();
                return false;
            }
        } catch (final NoSuchAlgorithmException e1) {
            String errorMessage = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DownloadFiles.checksum.NoSuchAlgorithmException", "No entry found for DownloadFiles.checksum.NoSuchAlgorithmException");
            e1.printStackTrace();
            logger.error(errorMessage);
            pkgmgr.addWarning(errorMessage);
        } catch (final IOException e) {
            String errorMessage = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DownloadFiles.checksum.IOException", "No entry found for DownloadFiles.checksum.IOException");
            e.printStackTrace();
            logger.error(errorMessage);
            pkgmgr.addWarning(errorMessage);
        }
        return true;
    }
}
