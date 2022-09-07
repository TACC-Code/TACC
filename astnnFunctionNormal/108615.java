class BackupThread extends Thread {
    static void add(JarOutputStream j, File base, File file) throws IOException {
        File f = new File(base, file.getPath());
        if (f.isDirectory()) {
            String[] children = f.list();
            if (children != null) for (String c : children) add(j, base, new File(file, c));
        } else {
            JarEntry e = new JarEntry(file.getPath());
            e.setSize(f.length());
            j.putNextEntry(e);
            j.write(read(f));
            j.closeEntry();
        }
    }
}
