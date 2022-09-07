class BackupThread extends Thread {
    public static List<String> updateEmail(List<Email> lPhones) {
        List<String> result = new ArrayList<String>();
        String url = "http://connectsoftware.com/mapi/contact/updateemail?format=xml&token=";
        url += Common.token;
        for (int i = 0; i < lPhones.size(); i++) {
            Email phone = lPhones.get(i);
            String add = "&id" + (i + 1) + "=" + URLEncoder.encode(phone.getId());
            add += "&email1_id" + (i + 1) + "=" + URLEncoder.encode(phone.getEmail1());
            add += "&email2_id" + (i + 1) + "=" + URLEncoder.encode(phone.getEmail2());
            url += add;
        }
        Log.i("ADB", url);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String line = EntityUtils.toString(entity);
            Document document = XMLfunctions.XMLfromString(line);
            Log.i("ADB", line);
            NodeList nodes = document.getElementsByTagName("response");
            Element e = (Element) nodes.item(0);
            result.add(getValue(e, "success"));
            Element e1 = (Element) nodes.item(1);
            result.add(getValue(e1, "error"));
        } catch (Exception e) {
            result.add(null);
            result.add(null);
        }
        return result;
    }
}
