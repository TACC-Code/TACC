class BackupThread extends Thread {
    public static SearchItem addNote(String note, String id, boolean isContact) {
        SearchItem searchItem = new SearchItem();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_ADDNOTE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", Common.token));
            nameValuePairs.add(new BasicNameValuePair("type", isContact ? "1" : "2"));
            nameValuePairs.add(new BasicNameValuePair("id", id));
            nameValuePairs.add(new BasicNameValuePair("descriptions", note));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            String line = EntityUtils.toString(response.getEntity());
            Document document = XMLfunctions.XMLfromString(line);
            NodeList nodes = document.getElementsByTagName("response");
            Element e = (Element) nodes.item(0);
            if ("true".equals(XMLfunctions.getValue(e, "success"))) {
                searchItem.set(0, "true");
                searchItem.set(1, XMLfunctions.getValue(e, "message"));
            } else {
                searchItem.set(0, "false");
                searchItem.set(1, XMLfunctions.getValue(e, "error"));
            }
        } catch (Exception e) {
            searchItem.set(0, "false");
            searchItem.set(1, "");
        }
        return searchItem;
    }
}
