class BackupThread extends Thread {
    private static void loadConverters(URL url) {
        try {
            if (url.toString().contains("jar!")) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                Enumeration<JarEntry> entries = connection.getJarFile().entries();
                while (entries.hasMoreElements()) {
                    String entryName = entries.nextElement().getName();
                    if (entryName.contains("/converter/") && entryName.endsWith(".class")) {
                        entryName = (entryName.split(".class")[0]).replace('/', '.');
                        Class<?> clazz = Class.forName(entryName);
                        JOFfreeConverter converter = (JOFfreeConverter) clazz.newInstance();
                        converters.put(converter.handles(), converter);
                    }
                }
            } else {
                File dir = new File(new URI(url.toString()));
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        loadConverters(file.toURL());
                    } else {
                        String className = file.getAbsolutePath();
                        if (className.endsWith(".class")) {
                            className = className.substring(className.indexOf("org"), className.lastIndexOf('.'));
                            className = className.replace('/', '.');
                            JOFfreeConverter converter = (JOFfreeConverter) Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
                            converters.put(converter.handles(), converter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOFfreeException ex = new JOFfreeException("error loading converters: ", e);
            ex.printStackTrace();
        }
    }
}
