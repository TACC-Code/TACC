class BackupThread extends Thread {
    public static String getWebPage(final URL url) {
        StringBuffer page = new StringBuffer();
        try {
            URLConnection connection = url.openConnection();
            String line;
            BufferedReader in;
            if (connection.getContentEncoding() == null) {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(url.openStream(), connection.getContentEncoding()));
            }
            while ((line = in.readLine()) != null) {
                page.append(line).append('\n');
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            System.err.println("WebPage.getWebPage(): " + e);
        } catch (IOException e) {
            System.err.println("WebPage.getWebPage(): " + e);
        }
        logger.warn("TOOLBOX:\t " + page.toString());
        return page.toString();
    }
}
