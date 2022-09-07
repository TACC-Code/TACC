class BackupThread extends Thread {
    public static void unzipToDir(String zipFile, String unzipDir) throws Exception {
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration en = zip.entries(); en.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) en.nextElement();
            String newname = unzipDir + File.separator + entry.getName();
            if (entry.isDirectory()) (new File(newname)).mkdirs(); else new File(newname).getParentFile().mkdirs();
        }
        for (Enumeration en = zip.entries(); en.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) en.nextElement();
            String newname = unzipDir + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                int len = 0;
                byte[] buffer = new byte[2048];
                InputStream in = zip.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newname));
                while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
                in.close();
                out.close();
            }
        }
        zip.close();
    }
}
