class BackupThread extends Thread {
    public static Set<String> findClassNames(URL url, String packagee, boolean deep) throws IOException {
        URLConnection conn = url.openConnection();
        if (conn instanceof JarURLConnection) {
            return findClassNames(((JarURLConnection) conn).getJarFile(), packagee, deep);
        }
        String urlProtocol = url.getProtocol();
        String urlFileName = url.getFile();
        int jarStringIndex = urlFileName.indexOf("!/");
        if (jarStringIndex > 0) {
            urlFileName = urlFileName.substring(0, jarStringIndex);
        }
        if (urlFileName.startsWith(urlProtocol + ":/")) {
            urlFileName = urlFileName.substring(urlProtocol.length() + 2);
        } else if (urlFileName.startsWith("file:/")) {
            urlFileName = urlFileName.substring(6);
        }
        if (urlProtocol.equalsIgnoreCase("jar") || urlProtocol.equalsIgnoreCase("jar") || urlFileName.endsWith(".jar") || urlFileName.endsWith(".zip")) {
            String jarFileName = URLDecoder.decode(urlFileName, System.getProperty("file.encoding"));
            File file = new File(jarFileName);
            if (file.exists()) {
                JarFile jar = new JarFile(file);
                try {
                    return findClassNames(new JarFile(file), packagee, deep);
                } finally {
                    try {
                        jar.close();
                    } catch (IOException e) {
                        ;
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("[mvc] -> jar '{}' not exists", jarFileName);
                }
            }
        } else {
            String fileName = URLDecoder.decode(urlFileName, System.getProperty("file.encoding"));
            File file = new File(fileName);
            if (file.exists()) {
                return findClassNames(file, packagee, deep);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("[mvc] -> classes dir '{}' not exists", fileName);
                }
            }
        }
        return new HashSet<String>();
    }
}
