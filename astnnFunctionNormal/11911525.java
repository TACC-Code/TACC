class BackupThread extends Thread {
    public static Object[] readPath(String fileName) {
        try {
            int len = 0;
            File file = new File(fileName);
            InputStream is = null;
            if (file.exists()) {
                is = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                len = (int) file.length();
            } else {
                URL url = null;
                URLConnection uc = null;
                uc = url.openConnection();
                uc.setUseCaches(true);
                if (fileName.endsWith(".zip")) {
                    try {
                        is = new ZipInputStream(new BufferedInputStream(uc.getInputStream(), 8192));
                        ZipEntry ze = ((ZipInputStream) is).getNextEntry();
                        len = (int) ze.getSize();
                    } catch (Exception ex) {
                        is = null;
                    }
                }
                if (is == null) {
                    len = uc.getContentLength();
                    is = new DataInputStream(new BufferedInputStream(uc.getInputStream(), 8192));
                }
            }
            return new Object[] { is, new Integer(len) };
        } catch (Exception e) {
            return null;
        }
    }
}
