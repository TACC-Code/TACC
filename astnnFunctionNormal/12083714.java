class BackupThread extends Thread {
    private void loadClassBytes(JarFile jis, JarEntry jarName) throws IOException {
        if (printLoadMessages) System.out.println("\t" + jarName);
        BufferedInputStream jarBuf = new BufferedInputStream(jis.getInputStream(jarName));
        ByteArrayOutputStream jarOut = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = jarBuf.read()) != -1) jarOut.write(b);
            classArrays.put(jarName.getName().substring(0, jarName.getName().length() - 6), jarOut.toByteArray());
        } catch (IOException ioe) {
            System.out.println("Error reading entry " + jarName);
        }
    }
}
