class BackupThread extends Thread {
    public static List<SearchItem> searchAll(boolean isContact) {
        List<SearchItem> _return = new ArrayList<SearchItem>();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(isContact ? URL_SEARCH_ALL_CONTACT : URL_SEARCH_ALL_COMPANY);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", Common.token));
            nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            String line = EntityUtils.toString(response.getEntity());
            Document document = XMLfunctions.XMLfromString(line);
            NodeList nodes = document.getElementsByTagName("item");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);
                String id = XMLfunctions.getValue(e, isContact ? "id" : "MasterID");
                String name = XMLfunctions.getValue(e, isContact ? "Name__Last__First_" : "Name");
                SearchItem searchItem = new SearchItem();
                searchItem.set(0, id);
                searchItem.set(1, name);
                _return.add(searchItem);
            }
        } catch (Exception e) {
        }
        return _return;
    }
}
