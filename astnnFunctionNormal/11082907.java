class BackupThread extends Thread {
    protected static void loadNativeLibrary() {
        String libName = "JNativeHook";
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError linkError) {
            try {
                String jarLibPath = "org/jnativehook/lib/" + NativeSystem.getFamily().toString().toLowerCase() + "/" + NativeSystem.getArchitecture().toString().toLowerCase() + "/";
                File classFile = new File(GlobalScreen.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsoluteFile();
                if (classFile.isFile()) {
                    JarFile jarFile = new JarFile(classFile);
                    JarEntry jarLibEntry = jarFile.getJarEntry(jarLibPath + System.mapLibraryName(libName));
                    File tmpLibFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator", File.separator) + System.mapLibraryName(libName));
                    InputStream jarInputStream = jarFile.getInputStream(jarLibEntry);
                    FileOutputStream tempLibOutputStream = new FileOutputStream(tmpLibFile);
                    byte[] array = new byte[8192];
                    int read = 0;
                    while ((read = jarInputStream.read(array)) > 0) {
                        tempLibOutputStream.write(array, 0, read);
                    }
                    tempLibOutputStream.close();
                    tmpLibFile.deleteOnExit();
                    System.load(tmpLibFile.getPath());
                } else if (classFile.isDirectory()) {
                    File libFolder = new File(classFile.getAbsoluteFile() + "/" + jarLibPath);
                    if (libFolder.isDirectory()) {
                        System.setProperty("java.library.path", System.getProperty("java.library.path", ".") + System.getProperty("path.separator", ":") + libFolder.getPath());
                        Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
                        sysPath.setAccessible(true);
                        if (sysPath != null) {
                            sysPath.set(System.class.getClassLoader(), null);
                        }
                        System.loadLibrary(libName);
                    }
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
