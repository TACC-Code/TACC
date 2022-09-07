class BackupThread extends Thread {
    private static Properties loadProperties(Properties p, String path) throws IOException {
        URL url;
        File file;
        String absolutePath;
        InputStream in;
        boolean skipWhenNotExists = false;
        if (path.startsWith("ifexists:")) {
            skipWhenNotExists = true;
            path = path.substring("ifexists:".length());
        }
        if (path.startsWith("classpath:")) {
            path = path.substring("classpath:".length());
            url = getClassLoader().getResource(path);
            if (url == null) {
                if (skipWhenNotExists) {
                    return p;
                }
                throw new FileNotFoundException("Not found " + path + " in classpath.");
            }
            file = new File(url.getFile());
            in = url.openStream();
        } else {
            if (path.startsWith("dic-home:")) {
                File dicHome = new File(getDicHome(p));
                path = path.substring("dic-home:".length());
                file = new File(dicHome, path);
            } else {
                file = new File(path);
            }
            if (skipWhenNotExists && !file.exists()) {
                return p;
            }
            in = new FileInputStream(file);
        }
        absolutePath = file.getAbsolutePath();
        p.load(in);
        in.close();
        String lastModifieds = p.getProperty("paoding.analysis.properties.lastModifieds");
        String files = p.getProperty("paoding.analysis.properties.files");
        String absolutePaths = p.getProperty("paoding.analysis.properties.files.absolutepaths");
        if (lastModifieds == null) {
            p.setProperty("paoding.dic.properties.path", path);
            lastModifieds = String.valueOf(getFileLastModified(file));
            files = path;
            absolutePaths = absolutePath;
        } else {
            lastModifieds = lastModifieds + ";" + getFileLastModified(file);
            files = files + ";" + path;
            absolutePaths = absolutePaths + ";" + absolutePath;
        }
        p.setProperty("paoding.analysis.properties.lastModifieds", lastModifieds);
        p.setProperty("paoding.analysis.properties.files", files);
        p.setProperty("paoding.analysis.properties.files.absolutepaths", absolutePaths);
        String importsValue = p.getProperty("paoding.imports");
        if (importsValue != null) {
            p.remove("paoding.imports");
            String[] imports = importsValue.split(";");
            for (int i = 0; i < imports.length; i++) {
                loadProperties(p, imports[i]);
            }
        }
        return p;
    }
}
