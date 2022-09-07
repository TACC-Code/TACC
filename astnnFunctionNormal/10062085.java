class BackupThread extends Thread {
    private void copyJarFilesToJar(JarOutputStream out, String jarSourceName) throws IOException {
        byte[] buffer = new byte[2048];
        InputStream isJar = getClass().getResourceAsStream("/" + jarSourceName);
        JarInputStream zin = new JarInputStream(isJar);
        JarEntry entry = null;
        while ((entry = zin.getNextJarEntry()) != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (true) {
                int read = zin.read(buffer);
                if (read == -1) break;
                bos.write(buffer, 0, read);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            addToJar(out, bais, entry.getName(), bos.toByteArray().length);
            bos.close();
            zin.closeEntry();
        }
        zin.close();
        isJar.close();
    }
}
