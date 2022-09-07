class BackupThread extends Thread {
    protected byte[] getByteCodes(String aClassname) throws ClassNotFoundException {
        String path = aClassname.replace('.', File.separatorChar) + DOT_CLASS;
        URL url = loader.findResource(path);
        if (null == url) {
            byte abyte0[] = getClassFromJarURLs(path);
            if (abyte0 == null) {
                throw new ClassNotFoundException(aClassname);
            } else {
                return abyte0;
            }
        } else {
            try {
                URLConnection conn = url.openConnection();
                return FileUtil.getByteArrayFromInputStream(conn.getInputStream(), conn.getContentLength());
            } catch (IOException ex) {
                throw new ClassNotFoundException(ex.getMessage());
            }
        }
    }
}
