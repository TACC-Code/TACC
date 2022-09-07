class BackupThread extends Thread {
    public static IDOMDocument parseURL(String url) {
        String docString = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            BufferedReader docReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer contents = new StringBuffer();
            String currLine = null;
            while ((currLine = docReader.readLine()) != null) {
                contents.append(currLine);
            }
            docString = contents.toString();
            docReader.close();
        } catch (IOException e) {
            System.out.println("Exception in InternetExplorer.parseURL(" + url + "):");
            e.printStackTrace();
        }
        return parseHTML(docString);
    }
}
