class BackupThread extends Thread {
            public void onClick(View v) {
                HttpPost httpPost = new HttpPost(prefs.getString("connection", "") + "person/" + person.getId() + "/");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("rstatus", "2"));
                    nameValuePairs.add(new BasicNameValuePair("firstName", firstName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("lastName", lastName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("address", address.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("city", city.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("state", state.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("country", country.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("mobile", mobile.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("birthDate", mDateDisplay.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("_method", "put"));
                    nameValuePairs.add(new BasicNameValuePair("male", String.valueOf(jkValue)));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    Log.i("Android JSON", response.getStatusLine().toString());
                    Log.i("Sending data to", httpPost.getURI().toString());
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result = new ConnectionManager().convertStreamToString(instream);
                        Log.i("Response", result);
                        instream.close();
                    }
                    Intent in = new Intent(Profile.this, DashBoard.class);
                    startActivity(in);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
}
