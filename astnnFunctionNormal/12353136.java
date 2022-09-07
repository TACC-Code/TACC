class BackupThread extends Thread {
        public void onReceive(Context c, Intent intent) {
            wifiList = mainWifi.getScanResults();
            for (int i = 0; i < wifiList.size(); i++) {
                Log.e("wifi", wifiList.get(i).toString());
            }
            HttpPost httpRequest = new HttpPost("http://www.google.com/loc/json");
            JSONObject holder = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                holder.put("version", "1.1.0");
                holder.put("host", "maps.google.com");
                holder.put("request_address", true);
                for (int i = 0; i < wifiList.size(); i++) {
                    JSONObject current_data = new JSONObject();
                    current_data.put("mac_address", wifiList.get(i).BSSID);
                    current_data.put("ssid", wifiList.get(i).SSID);
                    current_data.put("signal_strength", wifiList.get(i).level);
                    array.put(current_data);
                }
                holder.put("wifi_towers", array);
                Log.e("wifi", holder.toString());
                StringEntity se = new StringEntity(holder.toString());
                httpRequest.setEntity(se);
                HttpResponse resp = new DefaultHttpClient().execute(httpRequest);
                if (resp.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(resp.getEntity());
                    textview.setText(strResult);
                }
            } catch (JSONException e) {
                textview.setText(e.getMessage().toString());
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                textview.setText(e.getMessage().toString());
                e.printStackTrace();
            } catch (IOException e) {
                textview.setText(e.getMessage().toString());
                e.printStackTrace();
            } catch (Exception e) {
                textview.setText(e.getMessage().toString());
                e.printStackTrace();
            }
        }
}
