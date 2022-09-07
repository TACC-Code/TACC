class BackupThread extends Thread {
    public ArrayList<String> getFileList(String url) throws IOException {
        URL pic_url = new URL(url);
        ArrayList<String> links = new ArrayList<String>();
        String urlBase = "http://cs.indiana.edu/~echintha/b534/photos/";
        URLConnection pic_c = pic_url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(pic_c.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.isEmpty()) {
                continue;
            }
            links.add(urlBase + inputLine);
        }
        in.close();
        return links;
    }
}
