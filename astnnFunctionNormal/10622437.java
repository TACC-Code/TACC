class BackupThread extends Thread {
    protected static String getDocumentAt(String urlString) {
        StringBuffer html_text = new StringBuffer();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) html_text.append(line + "\n");
            reader.close();
            url = null;
            conn = null;
        } catch (MalformedURLException e) {
            System.out.println("��Ч��URL: " + urlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html_text.toString();
    }
}
