class BackupThread extends Thread {
    private static boolean putSyntaxCSS(File directory) throws IOException {
        File f;
        if (directory == null) {
            f = new File("syntax.css");
        } else {
            f = new File(directory, "syntax.css");
        }
        if (f.exists()) return false;
        FileOutputStream out = new FileOutputStream(f);
        InputStream in = ClassLoader.getSystemResourceAsStream("com/Ostermiller/Syntax/doc/syntax.css");
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
        return true;
    }
}
