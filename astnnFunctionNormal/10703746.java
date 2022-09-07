class BackupThread extends Thread {
    public static BufferedReader connect(URL url, boolean silent) throws IOException {
        if (!silent) System.out.print("Connecting " + url);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(Prefs.current.timeout);
        con.setRequestProperty("Accept-Language", Prefs.current.language);
        con.setReadTimeout(Prefs.current.timeout);
        BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("utf8")));
        if (!silent) System.out.print(", reading ");
        return r;
    }
}
