class BackupThread extends Thread {
    protected Vector<Class> find(String pckgname, boolean recursive, Vector<Class> classes) {
        URL url;
        String name = pckgname;
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.', '/');
        url = this.getClass().getResource(name);
        pckgname = pckgname + ".";
        File directory;
        try {
            directory = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (directory.exists()) {
            logger.info("Searching for Demo classes in \"" + directory.getName() + "\".");
            addAllFilesInDirectory(directory, classes, pckgname, recursive);
        } else {
            try {
                logger.info("Searching for Demo classes in \"" + url + "\".");
                URLConnection urlConnection = url.openConnection();
                if (urlConnection instanceof JarURLConnection) {
                    JarURLConnection conn = (JarURLConnection) urlConnection;
                    JarFile jfile = conn.getJarFile();
                    Enumeration e = jfile.entries();
                    while (e.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) e.nextElement();
                        Class result = load(entry.getName());
                        if (result != null && !classes.contains(result)) {
                            classes.add(result);
                        }
                    }
                }
            } catch (IOException e) {
                logger.logp(Level.SEVERE, this.getClass().toString(), "find(pckgname, recursive, classes)", "Exception", e);
            } catch (Exception e) {
                logger.logp(Level.SEVERE, this.getClass().toString(), "find(pckgname, recursive, classes)", "Exception", e);
            }
        }
        return classes;
    }
}
