class BackupThread extends Thread {
    public static String processSourceCode(String lang, String source) {
        source.replaceAll("\t", "    ");
        try {
            String postStr = "lang=" + URLEncoder.encode(lang, "UTF-8") + "&source=" + URLEncoder.encode(source, "UTF-8");
            URL url = new URL("http://xinetools.appspot.com/syntaxhighlight");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(postStr.getBytes());
            connection.getOutputStream().close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return source;
        }
    }
}
