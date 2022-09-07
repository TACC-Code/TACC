class BackupThread extends Thread {
    private Object getResource(ServletContext context, HttpServletRequest request, Document document, String path, String browser) throws ProcessorException {
        Object resource = null;
        Enumeration pis = Utils.getAllPIs(document, STYLESHEET_PI).elements();
        while (pis.hasMoreElements()) {
            Hashtable attributes = Utils.getPIPseudoAttributes((ProcessingInstruction) pis.nextElement());
            String type = (String) attributes.get("type");
            if ((type != null) && (type.equals("text/xsl"))) {
                String url = (String) attributes.get("href");
                if (url != null) {
                    Object local = null;
                    try {
                        if (url.charAt(0) == '/') {
                            local = new File(Utils.getRootpath(request, context) + url);
                        } else if (url.indexOf("://") < 0) {
                            local = new File(Utils.getBasepath(request, context) + url);
                        } else {
                            local = new URL(url);
                        }
                    } catch (MalformedURLException e) {
                        throw new ProcessorException("Could not associate stylesheet to document: " + url + " is a malformed URL.");
                    }
                    String media = (String) attributes.get("media");
                    if (media == null) {
                        resource = local;
                        if (browser == null) break;
                    } else if (browser != null) {
                        if (media.equals(browser)) {
                            resource = local;
                            break;
                        }
                    }
                }
            } else if ((type != null) && (type.equals("application/rmdms-raw"))) {
                Object access = attributes.get("access");
                if ("readwrite".equals(access)) {
                    resource = rawReadWrite;
                } else {
                    resource = rawReadOnly;
                }
            }
        }
        if (resource == null) {
            throw new ProcessorException("Could not associate stylesheet to document: " + " no matching stylesheet for: " + browser);
        } else {
            return resource;
        }
    }
}
