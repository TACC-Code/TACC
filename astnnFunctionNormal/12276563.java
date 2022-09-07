class BackupThread extends Thread {
    public static String fetchSettingFromServer(String tag) {
        try {
            if (pageCache == null) {
                URL url = new URL("http://code.google.com/p/marla/wiki/CurrentSettings");
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder page = new StringBuilder();
                String line = null;
                while ((line = rd.readLine()) != null) page.append(line);
                rd.close();
                pageCache = page.toString();
            }
            Pattern server = Pattern.compile("\\|" + tag + "\\|(.+)\\|" + tag + "\\|");
            Matcher m = server.matcher(pageCache.toString());
            if (!m.find()) return null;
            return m.group(1);
        } catch (IOException ex) {
            return null;
        }
    }
}
