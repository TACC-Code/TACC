class BackupThread extends Thread {
    public void positionByPhone() {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://www.google.com/loc/json");
            StringEntity se;
            se = new StringEntity("json=" + holder.toString());
            post.setEntity(se);
            HttpResponse resp = client.execute(post);
            HttpEntity he = resp.getEntity();
            InputStreamReader isr = new InputStreamReader(he.getContent());
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String result = br.readLine();
            while (result != null) {
                sb.append(result);
                result = br.readLine();
            }
            JSONObject d = new JSONObject(sb.toString());
            JSONObject location = new JSONObject();
            JSONObject address = new JSONObject();
            location = (JSONObject) d.get("location");
            address = (JSONObject) location.get("address");
        } catch (Exception e) {
        }
    }
}
