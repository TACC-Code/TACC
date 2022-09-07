class BackupThread extends Thread {
    public void run() {
        if (m_links == null) {
            return;
        }
        URL url = null;
        try {
            url = new URL(m_urlString);
        } catch (Exception e) {
            System.err.println("OnlineHelp.Worker.run (url) - " + e);
        }
        if (url == null) {
            return;
        }
        try {
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
            kit.read(new InputStreamReader(is), doc, 0);
            HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
            Object target = null;
            Object href = null;
            while ((it != null) && it.isValid()) {
                AttributeSet as = it.getAttributes();
                if ((target == null) || (href == null)) {
                    Enumeration en = as.getAttributeNames();
                    while (en.hasMoreElements()) {
                        Object o = en.nextElement();
                        if ((target == null) && o.toString().equals("target")) {
                            target = o;
                        } else if ((href == null) && o.toString().equals("href")) {
                            href = o;
                        }
                    }
                }
                if (target != null && "Online".equals(as.getAttribute(target))) {
                    String hrefString = (String) as.getAttribute(href);
                    if (hrefString != null) {
                        try {
                            String AD_Window_ID = hrefString.substring(hrefString.indexOf('/', 1), hrefString.lastIndexOf('/'));
                            m_links.put(AD_Window_ID, hrefString);
                        } catch (Exception e) {
                            System.err.println("OnlineHelp.Worker.run (help) - " + e);
                        }
                    }
                }
                it.next();
            }
            is.close();
        } catch (ConnectException e) {
        } catch (UnknownHostException uhe) {
        } catch (Exception e) {
            System.err.println("OnlineHelp.Worker.run (e) " + e);
        } catch (Throwable t) {
            System.err.println("OnlineHelp.Worker.run (t) " + t);
        }
    }
}
