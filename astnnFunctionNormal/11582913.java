class BackupThread extends Thread {
    private void loadAllResources(String name, Class cl, StreamLoader loader) {
        boolean anyLoaded = false;
        try {
            URL[] urls;
            ClassLoader cld = null;
            cld = getContextClassLoader();
            if (cld == null) cld = cl.getClassLoader();
            if (cld != null) urls = getResources(cld, name); else urls = getSystemResources(name);
            if (urls != null) {
                for (int i = 0; i < urls.length; i++) {
                    URL url = urls[i];
                    InputStream clis = null;
                    if (debug) pr("DEBUG: URL " + url);
                    try {
                        clis = openStream(url);
                        if (clis != null) {
                            loader.load(clis);
                            anyLoaded = true;
                            if (debug) pr("DEBUG: successfully loaded resource: " + url);
                        } else {
                            if (debug) pr("DEBUG: not loading resource: " + url);
                        }
                    } catch (IOException ioex) {
                        if (debug) pr("DEBUG: " + ioex);
                    } catch (SecurityException sex) {
                        if (debug) pr("DEBUG: " + sex);
                    } finally {
                        try {
                            if (clis != null) clis.close();
                        } catch (IOException cex) {
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (debug) pr("DEBUG: " + ex);
        }
        if (!anyLoaded) {
            if (debug) pr("DEBUG: !anyLoaded");
            loadResource("/" + name, cl, loader);
        }
    }
}
