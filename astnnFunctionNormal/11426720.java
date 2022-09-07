class BackupThread extends Thread {
    public String downloadFromUrl(URL url) {
        BufferedReader dis;
        String content = "";
        HttpURLConnection urlConn = null;
        try {
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(false);
            dis = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;
            while ((line = dis.readLine()) != null) {
                content = content.concat(line);
                content = content.concat("\n");
            }
        } catch (MalformedURLException ex) {
            System.err.println(ex + " (downloadFromUrl) " + url);
        } catch (java.io.IOException iox) {
            System.out.println(iox + " (downloadFromUrl) " + url);
        } catch (Exception generic) {
            System.out.println(generic.toString() + " (downloadFromUrl) " + url);
        } finally {
        }
        return content;
    }
}
