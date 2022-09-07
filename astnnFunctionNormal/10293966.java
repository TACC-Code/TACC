class BackupThread extends Thread {
    private List<LibraryJB> parseLibXml() {
        File appDir = context.getExternalFilesDir(null);
        File libCache = new File(appDir, "libResources.xml");
        if (libCache.exists() && (System.currentTimeMillis() - libCache.lastModified() <= refreshRate)) {
        } else {
        }
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://booktracker.googlecode.com/svn/trunk/libraries.xml");
        List<LibraryJB> retLibList = new ArrayList<LibraryJB>();
        try {
            HttpResponse resp = client.execute(get);
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(resp.getEntity().getContent(), "UTF-8");
            int parserEvent = parser.getEventType();
            String tagName = "";
            String argKey = "";
            LibraryJB jb = null;
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch(parserEvent) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("LIBRARY")) {
                            jb = new LibraryJB();
                        } else if (tagName.equalsIgnoreCase("ARG")) {
                            argKey = parser.getAttributeValue(0);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("LIBRARY")) {
                            retLibList.add(jb);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (parser.getText().trim().equals("")) {
                            break;
                        }
                        if (tagName.equalsIgnoreCase("NAME")) {
                            jb.setName(parser.getText());
                        } else if (tagName.equalsIgnoreCase("STATE")) {
                            jb.setState(parser.getText());
                        } else if (tagName.equalsIgnoreCase("CLASS")) {
                            jb.setClazz(parser.getText());
                        } else if (tagName.equalsIgnoreCase("ARG")) {
                            jb.getArgs().put(argKey, parser.getText());
                        }
                        break;
                    default:
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retLibList;
    }
}
