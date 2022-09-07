class BackupThread extends Thread {
    public static List<Phone> searchAllPhone(String _id) {
        List<Phone> _return = new ArrayList<Phone>();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_GET_PHONE_CONTACT);
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
                String phone1 = XMLfunctions.getValue(e, "phone1");
                String phone2 = XMLfunctions.getValue(e, "phone2");
                String name = XMLfunctions.getValue(e, "name");
                Phone phone = new Phone();
                phone.setId(id);
                phone.setName(name);
                phone.setPhone1(phone1);
                phone.setPhone2(phone2);
                _return.add(phone);
            }
        } catch (Exception e) {
        }
        return _return;
    }
}
