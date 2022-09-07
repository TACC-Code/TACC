class BackupThread extends Thread {
    private void getControllerClasses(String path, List<String> classes) throws Exception {
        Enumeration<URL> urls = getClass().getClassLoader().getResources(path);
        if (!urls.hasMoreElements()) {
            urls = getClass().getClassLoader().getResources("/" + path);
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            if (protocol.equalsIgnoreCase("file") || protocol.equalsIgnoreCase("vfsfile")) {
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                File file = new File(filePath);
                File[] ff = file.listFiles();
                for (File f : ff) {
                    String name = f.getName();
                    if (f.isDirectory()) {
                        getControllerClasses(path + "/" + name, classes);
                    } else if (name.endsWith(".class")) {
                        classes.add(path + "/" + name);
                    }
                }
            } else if (protocol.equalsIgnoreCase("jar") || protocol.equalsIgnoreCase("zip") || protocol.equalsIgnoreCase("wsjar")) {
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                    if (name.startsWith(path) && name.endsWith(".class")) {
                        classes.add(name);
                    }
                }
            }
        }
    }
}
