class BackupThread extends Thread {
    public ResourceWrapper(XfoafSscfResource _resource, String _serviceAddr, boolean isBookmark) {
        this.resource = _resource;
        this.uri = resource.getUri().toString();
        this.label = resource.getLabel();
        this.serviceAddr = _serviceAddr;
        try {
            Class clBookFactory = Class.forName("org.jeromedl.marcont.book.BookFactory");
            Method mLoadBook = clBookFactory.getMethod("loadBook", new Class[] { String.class });
            Class clBook = Class.forName("org.jeromedl.marcont.book.BookInterface");
            Method mGetPersons = clBook.getMethod("getPersons", new Class[0]);
            Method mGetTitle = clBook.getMethod("getTitle", new Class[0]);
            bookRes = mLoadBook.invoke(null, new Object[] { this.uri });
            authors = getAuthorsAsString((Person[]) mGetPersons.invoke(bookRes, new Object[0]));
            title = (String) mGetTitle.invoke(bookRes, new Object[0]);
        } catch (Exception e) {
            if (isBookmark) {
                if (serviceAddr == null) {
                    InputStream is = Context.class.getClassLoader().getResourceAsStream("foafrealm_context.properties");
                    Properties prop = new Properties();
                    try {
                        prop.load(is);
                        is.close();
                    } catch (Exception ioex) {
                        log.warning("Context - error loading preferences: foafrealm_context");
                    }
                    serviceAddr = prop.getProperty("default_server_address");
                }
                String servletUrl = serviceAddr + "servlet/getBook?uri=" + uri;
                try {
                    URL url = new URL(servletUrl);
                    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                    huc.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                    String getLine = br.readLine();
                    if (getLine != null && !"".equals(getLine.trim())) {
                        allInOne = getLine.split(";;");
                        if (allInOne != null && allInOne.length > 1) {
                            authors = allInOne[0];
                            title = allInOne[1];
                        }
                    }
                    br.close();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
