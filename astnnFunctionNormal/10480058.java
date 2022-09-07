class BackupThread extends Thread {
    public static SearchItem addEmploy(String contactId, String publication, String job_type) {
        SearchItem searchItem = new SearchItem();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_ADDEMPLOY);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", Common.token));
            nameValuePairs.add(new BasicNameValuePair("contact_id", contactId));
            nameValuePairs.add(new BasicNameValuePair("publication_id", publication));
            nameValuePairs.add(new BasicNameValuePair("job_type", job_type));
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
            searchItem.set(0, "true");
            searchItem.set(1, "");
        }
        return searchItem;
    }
}
