class BackupThread extends Thread {
    public static File getClasspathDir(Class clazz) throws GorillaException {
        String className = clazz.getName();
        String resourceName = "/" + className.replaceAll("\\.", "/") + ".class";
        URL url = clazz.getResource(resourceName);
        if (url == null) {
            throw new GorillaException("could not bootstrap root of classpath" + " for class" + className);
        }
        String urlString = url.toString();
        urlString = urlString.substring(0, urlString.lastIndexOf(resourceName) + 1);
        try {
            url = new URL(urlString);
        } catch (java.net.MalformedURLException e) {
            throw new GorillaException(e.getMessage());
        }
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            throw new GorillaException("IOException attempting to open classpath" + " \"" + url.toString() + "\":" + e.getMessage());
        }
        String urlFileName = null;
        if (conn instanceof JarURLConnection) {
            url = ((JarURLConnection) conn).getJarFileURL();
            urlFileName = url.getFile();
            urlFileName = urlFileName.substring(0, urlFileName.lastIndexOf("/"));
        } else {
            urlFileName = url.getFile();
        }
        String bootFullPath = "could not parse url for file: " + urlFileName;
        try {
            bootFullPath = URLDecoder.decode(urlFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GorillaException(bootFullPath + ": " + e.getMessage());
        }
        File file = new File(bootFullPath);
        return file;
    }
}
