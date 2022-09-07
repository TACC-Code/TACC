class BackupThread extends Thread {
    private void sendToImKayac(ImodeMail mail) {
        String text = mail.getBody();
        if (mail.isDecomeFlg()) {
            text = Util.html2text(text);
        }
        text = imKayacHeader(mail) + text;
        if (text.length() > 512) {
            text = text.substring(0, 511);
        }
        log.info("Try to sending IM to [" + this.username + "] via im.kayac.com");
        try {
            HttpPost post = new HttpPost(ImKayacUrl + this.username);
            String digest = encrypt(text + this.secret);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("message", text));
            formparams.add(new BasicNameValuePair("sig", digest));
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            } catch (Exception e) {
            }
            post.setHeader("User-Agent", "imoten/1.5 (immfImKayacNotifier 0.1;)");
            post.setEntity(entity);
            try {
                HttpResponse res = this.httpClient.execute(post);
                if (res == null) {
                    log.info("im.kayac.com error - response is null");
                }
                if (res.getStatusLine().getStatusCode() != 200) {
                    log.info("im.kayac.com error - bad status code " + res.getStatusLine().getStatusCode());
                }
                JSONObject json = JSONObject.fromObject(EntityUtils.toString(res.getEntity(), "UTF-8"));
                String result = json.getString("result");
                if (!result.equals("posted")) {
                    log.debug(json.toString(2));
                    log.info("im.kayac.com error");
                } else {
                    log.info("Send IM to [" + this.username + "] via im.kayac.com complete!");
                }
            } finally {
                post.abort();
            }
        } catch (Exception e) {
            log.error("im.kayac Error.", e);
        }
    }
}
