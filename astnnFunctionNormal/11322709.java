class BackupThread extends Thread {
    private BuildInfo() {
        String javaHome = System.getProperty("java.home");
        List<LibraryInfo> infos = new ArrayList<LibraryInfo>();
        Class<? extends BuildInfo> clazz = getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                LibraryInfo info = readManifest(url.openStream());
                if (info != null && url.getFile().indexOf(javaHome) < 0) {
                    infos.add(info);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read BUILD file.", e);
        }
        _libraryInfos = Collections.unmodifiableList(infos);
    }
}
