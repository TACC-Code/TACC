class BackupThread extends Thread {
    public String attributeTraining(int attribId) {
        String returnValue = null;
        try {
            get = new HttpGet(fidUrl + "/Player/PlayerGeneral/UA1.aspx");
            page = client.execute(get, responseHandler);
            in = new BufferedReader(new StringReader(page));
            tmp = "";
            while ((tmp = in.readLine()) != null) {
                if (tmp.contains("__VIEWSTATE")) {
                    viewState = tmp.substring(tmp.lastIndexOf("value=\"") + 7, tmp.lastIndexOf("\""));
                } else if (tmp.contains("__EVENTVALIDATION")) {
                    eventValidation = tmp.substring(tmp.lastIndexOf("value=\"") + 7, tmp.lastIndexOf("\""));
                }
            }
            HttpPost httpost = new HttpPost(fidUrl + "/Player/PlayerGeneral/UA1.aspx");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("__VIEWSTATE", viewState));
            nvps.add(new BasicNameValuePair("__EVENTVALIDATION", eventValidation));
            nvps.add(new BasicNameValuePair("__EVENTTARGET", "M$M$M$C$C$C$headerPanel$trainType"));
            nvps.add(new BasicNameValuePair("M$M$M$C$C$C$headerPanel$trainType", "Attribute training"));
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(httpost);
            if (!response.getStatusLine().toString().equals("HTTP/1.1 200 OK")) {
                EntityUtils.consume(response.getEntity());
                throw new Exception("Something got wrong (" + response.getStatusLine().toString() + ").");
            }
            String tmp = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("__CALLBACKID", "M$M$M$C$C$C$headerPanel$gridViewAttributeTraining"));
            nvps.add(new BasicNameValuePair("__CALLBACKPARAM", "c0:GB|25;14|CUSTOMCALLBACK6|" + attribId + ";"));
            while ((tmp = in.readLine()) != null) {
                if (tmp.contains("__VIEWSTATE")) {
                    nvps.add(new BasicNameValuePair("__VIEWSTATE", tmp.split("\"")[7]));
                } else if (tmp.contains("CallbackState")) {
                    nvps.add(new BasicNameValuePair("M$M$M$C$C$C$headerPanel$" + "gridViewAttributeTraining$CallbackState", tmp.split("\"")[7]));
                } else if (tmp.contains("__EVENTVALIDATION")) {
                    nvps.add(new BasicNameValuePair("__EVENTVALIDATION", tmp.split("\"")[7]));
                }
            }
            nvps.add(new BasicNameValuePair("M$M$M$C$C$C$headerPanel$trainType", "Attribute training"));
            EntityUtils.consume(response.getEntity());
            httpost = new HttpPost(fidUrl + "/Player/PlayerGeneral/UA1.aspx");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = client.execute(httpost);
            if (!response.getStatusLine().toString().equals("HTTP/1.1 200 OK")) {
                EntityUtils.consume(response.getEntity());
                throw new Exception("Something got wrong.");
            }
            EntityUtils.consume(response.getEntity());
            nvps.set(0, new BasicNameValuePair("__CALLBACKID", "M$M$M$C$C$C$headerPanel"));
            nvps.set(1, new BasicNameValuePair("__CALLBACKPARAM", "c0:"));
            httpost = new HttpPost(fidUrl + "/Player/PlayerGeneral/UA1.aspx");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = client.execute(httpost);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            tmp = in.readLine();
            String[] array = tmp.split("(\">)|(</)");
            if (array.length >= 12) returnValue = array[11];
            if (returnValue.contains("label")) if (array.length >= 17) returnValue = array[16] + ".";
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
