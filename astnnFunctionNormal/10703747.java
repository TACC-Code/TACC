class BackupThread extends Thread {
    public static BufferedReader connectAsMozilla(URL url, boolean silent) throws IOException {
        if (!silent) System.out.print("Connecting " + url);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(Prefs.current.timeout);
        con.setReadTimeout(Prefs.current.timeout);
        con.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        con.setRequestProperty("Accept-Language", Prefs.current.language);
        con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.8.0.5) Gecko/20060719 Firefox/1.5.0.5");
        BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("utf8")));
        if (!silent) System.out.print(", reading ");
        return r;
    }
}
