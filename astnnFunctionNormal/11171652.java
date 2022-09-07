class BackupThread extends Thread {
    public void googlesearch() {
        Preferences prefs;
        String query;
        String terms;
        StringBuffer buffer;
        HttpURLConnection connection;
        URL url;
        Lexer lexer;
        URL[][] results;
        prefs = Preferences.userNodeForPackage(getClass());
        query = prefs.get(GOOGLEQUERY, DEFAULTGOOGLEQUERY);
        try {
            query = (String) JOptionPane.showInputDialog(this, "Enter the search term:", "Search Google", JOptionPane.PLAIN_MESSAGE, null, null, query);
            if (null != query) {
                terms = query.replace(' ', '+');
                buffer = new StringBuffer(1024);
                buffer.append("http://www.google.ca/search?");
                buffer.append("q=");
                buffer.append(terms);
                buffer.append("&ie=UTF-8");
                buffer.append("&oe=UTF-8");
                buffer.append("&hl=en");
                buffer.append("&btnG=Google+Search");
                buffer.append("&meta=");
                url = new URL(buffer.toString());
                connection = (HttpURLConnection) url.openConnection();
                if (USE_MOZILLA_HEADERS) {
                    connection.setRequestProperty("Referer", "http://www.google.ca");
                    connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,video/x-mng,image/png,image/jpeg,image/gif;q=0.2,text/css,*/*;q=0.1");
                    connection.setRequestProperty("Accept-Language", "en-us, en;q=0.50");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.2.1) Gecko/20030225");
                    connection.setRequestProperty("Accept-Charset", "ISO-8859-1, utf-8;q=0.66, *;q=0.66");
                } else {
                    connection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                    connection.setRequestProperty("Accept-Language", "en-ca");
                    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; trieste; .NET CLR 1.1.4322; .NET CLR 1.0.3705)");
                }
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                lexer = new Lexer(connection);
                results = getThumbelina().extractImageLinks(lexer, url);
                getThumbelina().reset();
                for (int i = 0; i < results[1].length; i++) {
                    String found = results[1][i].toExternalForm();
                    if (-1 == found.indexOf("google")) getThumbelina().append(results[1][i]);
                }
                prefs.put(GOOGLEQUERY, query);
                try {
                    prefs.flush();
                } catch (BackingStoreException bse) {
                    bse.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
