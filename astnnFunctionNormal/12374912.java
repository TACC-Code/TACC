class BackupThread extends Thread {
    private void loadDrivers(URL url, boolean isInternal) {
        String path;
        Digester digester = new Digester();
        digester.setValidating(false);
        path = "Drivers/Driver";
        digester.addObjectCreate(path, "org.sqsh.SQLDriver");
        digester.addSetNext(path, "addDriver", "org.sqsh.SQLDriver");
        digester.addCallMethod(path, "setName", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0, "name");
        digester.addCallMethod(path, "setUrl", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0, "url");
        digester.addCallMethod(path, "setDriverClass", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0, "class");
        digester.addCallMethod(path, "setTarget", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0, "target");
        digester.addCallMethod(path, "setAnalyzer", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0, "analyzer");
        path = "Drivers/Driver/Variable";
        digester.addCallMethod(path, "setVariable", 2, new Class[] { java.lang.String.class, java.lang.String.class });
        digester.addCallParam(path, 0, "name");
        digester.addCallParam(path, 1);
        path = "Drivers/Driver/SessionVariable";
        digester.addCallMethod(path, "setSessionVariable", 2, new Class[] { java.lang.String.class, java.lang.String.class });
        digester.addCallParam(path, 0, "name");
        digester.addCallParam(path, 1);
        path = "Drivers/Driver/Property";
        digester.addCallMethod(path, "setProperty", 2, new Class[] { java.lang.String.class, java.lang.String.class });
        digester.addCallParam(path, 0, "name");
        digester.addCallParam(path, 1);
        digester.push(this);
        InputStream in = null;
        try {
            in = url.openStream();
            digester.parse(in);
        } catch (Exception e) {
            System.err.println("Failed to parse driver file '" + url.toString() + "': " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
}
