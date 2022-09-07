class BackupThread extends Thread {
    @MetaData(required = false)
    public void setGroupNames(Object names) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String[] parents;
        if (names instanceof String) {
            parents = ((String) names).split(",");
        } else if (names instanceof List) {
            List<?> lnames = (List<?>) names;
            parents = new String[lnames.size()];
            lnames.toArray(parents);
        } else if (names instanceof String[]) {
            parents = (String[]) names;
        } else {
            throw new IllegalArgumentException("Names must be a String or a List, but is " + (names == null ? "null" : (names.getClass().toString())));
        }
        stgParents = new StringTemplateGroup[parents.length];
        StringTemplateGroup parentSTG = null;
        int refresh = this.getRefreshInterval();
        for (int i = parents.length - 1; i >= 0; i--) {
            parents[i] = parents[i].trim();
            URL url = cl.getResource(parents[i]);
            if (url == null) {
                throw new IllegalArgumentException("No resource named " + parents[i]);
            }
            String rootDir = url.getFile();
            StringTemplateGroup ret;
            if (i > 0) {
                if (url.toString().endsWith(".stg")) {
                    try {
                        log.info("Loading string template group file {}", url);
                        InputStreamReader r = new InputStreamReader(url.openStream());
                        ret = new StringTemplateGroup(r);
                        ret.setAttributeRenderers(StringSafeRenderer.RENDERERS);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read " + url);
                    }
                    ret.setSuperGroup(parents[i]);
                } else {
                    log.info("Loading string template directory {}", rootDir);
                    ret = new StringTemplateGroup(parents[i], rootDir);
                    ret.setAttributeRenderers(StringSafeRenderer.RENDERERS);
                }
                if (refresh > 0) ret.setRefreshInterval(refresh);
            } else {
                ret = this;
                setName(parents[i]);
                setRootDir(rootDir);
            }
            stgParents[i] = ret;
            log.info("root resource for {} is {}", parents[i], rootDir);
            if (rootDir == null || isArchivePath(rootDir)) {
                log.info("Using root resource {}", parents[i]);
                ret.setRootResource(parents[i]);
                ret.setRootDir(null);
            }
            if (parentSTG != null) ret.setSuperGroup(parentSTG);
            parentSTG = ret;
        }
        log.info("Name is " + this.getName() + " parents[0]=" + parents[0]);
    }
}
