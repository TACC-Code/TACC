class BackupThread extends Thread {
    public static final byte[] zipExtract(byte[] zipfile, String name) {
        byte[] res = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(zipfile);
            InputStream in = new BufferedInputStream(bis);
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                if (e.getName().equalsIgnoreCase(name)) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte b[] = new byte[TEMP_FILE_BUFFER_SIZE];
                    int len = 0;
                    while ((len = zin.read(b)) != -1) out.write(b, 0, len);
                    res = out.toByteArray();
                    break;
                }
            }
            zin.close();
        } catch (FileNotFoundException e) {
            MLUtil.runtimeError(e, "extractZip " + zipfile + " " + name);
        } catch (IOException e) {
            MLUtil.runtimeError(e, "extractZip " + zipfile + " " + name);
        }
        return (res);
    }
}
