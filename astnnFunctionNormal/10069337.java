class BackupThread extends Thread {
    public HtmlSpider(String urlSite) {
        try {
            InputStream in = null;
            if (!isArchivo) {
                URL url = new URL(urlSite);
                in = url.openStream();
            } else {
                File file = new File(urlSite);
                in = new FileInputStream(file);
            }
            InputStreamReader isr = new InputStreamReader(in);
            hsp = new HtmlSpiderParser(new HtmlPage());
            ParserDelegator pd = new ParserDelegator();
            pd.parse(isr, hsp, true);
            isr.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
