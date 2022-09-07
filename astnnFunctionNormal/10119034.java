class BackupThread extends Thread {
    private static void copyClassFile(File destdir, String classFileName) throws IOException {
        File destFile = new File(destdir, classFileName);
        if (destFile.exists()) return;
        FileOutputStream out = new FileOutputStream(destFile);
        InputStream in = Browser.class.getResourceAsStream("/pspdash/data/" + classFileName);
        byte[] buffer = new byte[3000];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) > -1) out.write(buffer, 0, bytesRead);
        out.close();
        in.close();
    }
}
