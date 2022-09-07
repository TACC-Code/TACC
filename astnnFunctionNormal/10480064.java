class BackupThread extends Thread {
    public static List<Email> searchAllEmail(String _id) {
        List<Email> _return = new ArrayList<Email>();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://connectsoftware.com/mapi/contact/getemail");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("format", "xml"));
            nameValuePairs.add(new BasicNameValuePair("token", Common.token));
            nameValuePairs.add(new BasicNameValuePair("id", _id));
            nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            String line = EntityUtils.toString(response.getEntity());
            Document document = XMLfunctions.XMLfromString(line);
            NodeList nodes = document.getElementsByTagName("item");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);
                String id = XMLfunctions.getValue(e, "id");
                String email1 = XMLfunctions.getValue(e, "email1");
                String email2 = XMLfunctions.getValue(e, "email2");
                String name = XMLfunctions.getValue(e, "name");
                Email email = new Email();
                email.setId(id);
                email.setEmail1(email1);
                email.setEmail2(email2);
                email.setName(name);
                _return.add(email);
            }
        } catch (Exception e) {
        }
        return _return;
    }
}
