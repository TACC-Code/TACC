class BackupThread extends Thread {
    private AgletClassLoader getClassLoaderInCache(URL codebase, Certificate owner, ClassName[] table) {
        Certificate[] owners;
        if (owner != null) {
            owners = new Certificate[] { owner };
        } else {
            owners = new Certificate[0];
        }
        CodeSource cs = new CodeSource(codebase, owners);
        Vector v = (Vector) this._map.get(cs);
        logger.debug("Looking for cached loader: " + codebase);
        if ((table == null) && JarAgletClassLoader.isJarFile(codebase)) {
            logger.debug("Codebase is jar file.");
            try {
                final URL fCodebase = codebase;
                ClassName[] tmpTab = (ClassName[]) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                    @Override
                    public Object run() throws IOException {
                        java.io.InputStream in = fCodebase.openStream();
                        JarArchive jar = new JarArchive(in);
                        Archive.Entry ae[] = jar.entries();
                        ClassName[] tab = new ClassName[ae.length];
                        for (int i = 0; i < ae.length; i++) {
                            tab[i] = new ClassName(ae[i].name(), DigestTable.toByteArray(ae[i].digest()));
                        }
                        in.close();
                        return tab;
                    }
                });
                table = new ClassName[tmpTab.length];
                System.arraycopy(tmpTab, 0, table, 0, table.length);
            } catch (PrivilegedActionException ex) {
                logger.error(ex);
            }
        }
        if (v != null) {
            Enumeration e = v.elements();
            while (e.hasMoreElements()) {
                AgletClassLoader loader = (AgletClassLoader) e.nextElement();
                if ((table == null) || loader.matchAndImport(table)) {
                    return loader;
                }
            }
        }
        return null;
    }
}
