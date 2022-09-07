class BackupThread extends Thread {
    public synchronized Iterable<? extends ContainedClass> getClasses() {
        if (containedClasses.isEmpty()) {
            switch(containerType) {
                case DIR:
                    {
                        final File base;
                        try {
                            base = new File(url.toURI()).getCanonicalFile();
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                        final int len = base.getAbsolutePath().length();
                        final Queue<File> folders = new LinkedList<File>();
                        folders.add(base);
                        while (!folders.isEmpty()) {
                            final File[] files = folders.poll().listFiles();
                            for (File file : files) {
                                if (file.isDirectory()) folders.add(file); else if (file.getName().endsWith(".class")) {
                                    try {
                                        containedClasses.add(new ContainedClassImpl(this, file.getCanonicalPath().substring(len + 1)));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e.getMessage(), e);
                                    }
                                }
                            }
                        }
                        break;
                    }
                case JAR_LOCAL:
                    {
                        JarFile jarFile = null;
                        try {
                            jarFile = new JarFile(new File(url.toURI()));
                            add(jarFile);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage(), e);
                        } finally {
                            if (jarFile != null) try {
                                jarFile.close();
                            } catch (IOException ignored) {
                            }
                        }
                        break;
                    }
                case JAR_REMOTE:
                    {
                        try {
                            JarFile jarFile = ((JarURLConnection) new URL("jar", "", url + "!/").openConnection()).getJarFile();
                            add(jarFile);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                        break;
                    }
            }
        }
        return containedClasses;
    }
}
