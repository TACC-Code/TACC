class BackupThread extends Thread {
    public static SearchItem editCompay(boolean isContact, String... args) {
        SearchItem searchItem = new SearchItem();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(isContact ? URL_EDIT_CONTACT : URL_EDIT_COOMPANY);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", Common.token));
            nameValuePairs.add(new BasicNameValuePair("id", args[0]));
            if (isContact) {
                nameValuePairs.add(new BasicNameValuePair("type", "1"));
                nameValuePairs.add(new BasicNameValuePair("email", args[1]));
                nameValuePairs.add(new BasicNameValuePair("phone1", args[2]));
                nameValuePairs.add(new BasicNameValuePair("phone2", args[3]));
                nameValuePairs.add(new BasicNameValuePair("fax", args[4]));
                nameValuePairs.add(new BasicNameValuePair("address1", args[5]));
                nameValuePairs.add(new BasicNameValuePair("address2", args[6]));
                nameValuePairs.add(new BasicNameValuePair("city", args[7]));
                nameValuePairs.add(new BasicNameValuePair("state", args[8]));
                nameValuePairs.add(new BasicNameValuePair("zip", args[9]));
                nameValuePairs.add(new BasicNameValuePair("profile", args[10]));
            } else {
                nameValuePairs.add(new BasicNameValuePair("name", args[1]));
                nameValuePairs.add(new BasicNameValuePair("email", args[2]));
                nameValuePairs.add(new BasicNameValuePair("phone", args[3]));
                nameValuePairs.add(new BasicNameValuePair("fax", args[4]));
                nameValuePairs.add(new BasicNameValuePair("address1", args[5]));
                nameValuePairs.add(new BasicNameValuePair("address2", args[6]));
                nameValuePairs.add(new BasicNameValuePair("city", args[7]));
                nameValuePairs.add(new BasicNameValuePair("state", args[8]));
                nameValuePairs.add(new BasicNameValuePair("zip", args[9]));
                nameValuePairs.add(new BasicNameValuePair("profile", args[10]));
            }
            nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
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
