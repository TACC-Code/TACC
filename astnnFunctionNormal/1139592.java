class BackupThread extends Thread {
        @Override
        protected ArrayList<String> doInBackground(CharSequence... params) {
            String username = params[0] + "";
            ArrayList<String> nowPlayingStatuses = new ArrayList<String>();
            try {
                URL url = new URL("http://twitter.com/statuses/user_timeline/" + URLEncoder.encode(username, "UTF-8") + ".json?count=200");
                String response = Util.convertStreamToString(url.openStream());
                JSONArray statuses = new JSONArray(response);
                for (int i = 0; i < statuses.length(); i++) {
                    JSONObject status = statuses.getJSONObject(i);
                    String statusText = status.getString("text");
                    if (statusText.toLowerCase().contains("#nowplaying")) {
                        nowPlayingStatuses.add(statusText);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                cancel(false);
            } catch (IOException e) {
                e.printStackTrace();
                cancel(false);
            } catch (JSONException e) {
                e.printStackTrace();
                cancel(false);
            }
            return nowPlayingStatuses;
        }
}
