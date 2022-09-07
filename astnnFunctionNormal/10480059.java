class BackupThread extends Thread {
    public static List<SearchItem> searchAllContact() {
        List<SearchItem> _return = new ArrayList<SearchItem>();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_ADDNOTE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", Common.token));
            nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            String line = EntityUtils.toString(response.getEntity());
            Document document = XMLfunctions.XMLfromString(line);
            try {
                NodeList nodes = document.getElementsByTagName("item");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element e = (Element) nodes.item(i);
                    String id = XMLfunctions.getValue(e, "id");
                    String name = XMLfunctions.getValue(e, "Name__Last__First_");
                    SearchItem searchItem = new SearchItem();
                    searchItem.set(0, id);
                    searchItem.set(1, name);
                    _return.add(searchItem);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return _return;
    }
}
