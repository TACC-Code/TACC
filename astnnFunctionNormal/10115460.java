class BackupThread extends Thread {
    public static List upzip(ZipInputStream zip, String destPath) throws ZipException, IOException {
        List fileNames = new ArrayList();
        ZipEntry azipfile = null;
        while ((azipfile = zip.getNextEntry()) != null) {
            String name = azipfile.getName();
            fileNames.add(name);
            if (!azipfile.isDirectory()) {
                File targetFile = new File(destPath, name);
                targetFile.getParentFile().mkdirs();
                if (targetFile.exists()) {
                    targetFile.delete();
                }
                targetFile.createNewFile();
                BufferedOutputStream diskfile = new BufferedOutputStream(new FileOutputStream(targetFile));
                byte[] buffer = new byte[1024];
                int read;
                while ((read = zip.read(buffer)) != -1) {
                    diskfile.write(buffer, 0, read);
                }
                diskfile.close();
            }
        }
        return fileNames;
    }
}
