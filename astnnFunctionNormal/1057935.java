class BackupThread extends Thread {
    public String getHtmlPage(URL url) {
        String html = null;
        try {
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Cookie", "language=en; searchTitle=1");
            BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            html = sb.toString().replaceAll("&nbsp;", " ").replaceAll("&amp;", "&");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
}
