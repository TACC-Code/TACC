class BackupThread extends Thread {
    public static List<Product> listActions() {
        InputStream is = null;
        String result = "";
        List<Product> productList = new ArrayList<Product>();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(ACTION_URL);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("m", "1: " + e.getMessage());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.d("m", result);
        } catch (Exception e) {
            Log.e("m", "2: " + e.getMessage());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Product product = new Product();
                product.setId(json_data.getInt("id"));
                product.setName(json_data.getString("name"));
                product.setPrice(json_data.getInt("price"));
                product.setImage(json_data.getString("image"));
                productList.add(product);
            }
        } catch (JSONException e) {
            Log.e("m", "3: " + e.getMessage());
        }
        return productList;
    }
}
