class BackupThread extends Thread {
    public synchronized void sendMail(SenderMail mail, boolean forcePlaintext) throws IOException, LoginException {
        if (this.logined == null) {
            log.warn("iモード.netにログインできていません。");
            this.logined = Boolean.FALSE;
            throw new LoginException("imode.net nologin");
        }
        if (this.logined != null && this.logined == Boolean.FALSE) {
            try {
                this.login();
            } catch (LoginException e) {
                throw new IOException("Could not login to imode.net");
            }
        }
        List<String> inlineFileIdList = new LinkedList<String>();
        List<String> attachmentFileIdList = new LinkedList<String>();
        if (!forcePlaintext) {
            for (SenderAttachment file : mail.getInlineFile()) {
                String docomoFileId = this.sendAttachFile(inlineFileIdList, true, file.getContentTypeWithoutParameter(), file.getFilename(), file.getData());
                file.setDocomoFileId(docomoFileId);
            }
        }
        List<SenderAttachment> attachFiles = mail.getAttachmentFile();
        for (SenderAttachment file : attachFiles) {
            this.sendAttachFile(attachmentFileIdList, false, file.getContentTypeWithoutParameter(), file.getFilename(), file.getData());
        }
        boolean htmlMail = false;
        String body = null;
        if (forcePlaintext) {
            htmlMail = false;
            body = mail.getPlainBody();
        } else {
            body = mail.getHtmlBody(true);
            if (body == null) {
                htmlMail = false;
                body = mail.getPlainBody();
            } else {
                htmlMail = true;
            }
        }
        if (htmlMail) {
            body = HtmlConvert.replaceAllCaseInsenstive(body, "<img src=\"cid:[^>]*>", "");
        }
        log.info("Html " + htmlMail);
        log.info("body " + body);
        MultipartEntity multi = new MultipartEntity();
        try {
            multi.addPart("folder.id", new StringBody("0", Charset.forName("UTF-8")));
            String mailType = null;
            if (htmlMail) {
                mailType = "1";
            } else {
                mailType = "0";
            }
            multi.addPart("folder.mail.type", new StringBody(mailType, Charset.forName("UTF-8")));
            int recipient = 0;
            for (InternetAddress ia : mail.getTo()) {
                multi.addPart("folder.mail.addrinfo(" + recipient + ").mladdr", new StringBody(ia.getAddress(), Charset.forName("UTF-8")));
                multi.addPart("folder.mail.addrinfo(" + recipient + ").type", new StringBody("1", Charset.forName("UTF-8")));
                recipient++;
            }
            for (InternetAddress ia : mail.getCc()) {
                multi.addPart("folder.mail.addrinfo(" + recipient + ").mladdr", new StringBody(ia.getAddress(), Charset.forName("UTF-8")));
                multi.addPart("folder.mail.addrinfo(" + recipient + ").type", new StringBody("2", Charset.forName("UTF-8")));
                recipient++;
            }
            for (InternetAddress ia : mail.getBcc()) {
                multi.addPart("folder.mail.addrinfo(" + recipient + ").mladdr", new StringBody(ia.getAddress(), Charset.forName("UTF-8")));
                multi.addPart("folder.mail.addrinfo(" + recipient + ").type", new StringBody("3", Charset.forName("UTF-8")));
                recipient++;
            }
            if (recipient > 5) {
                throw new IOException("Too Much Recipient");
            }
            multi.addPart("folder.mail.subject", new StringBody(Util.reverseReplaceUnicodeMapping(mail.getSubject()), Charset.forName("UTF-8")));
            body = Util.reverseReplaceUnicodeMapping(body);
            if (body.getBytes().length > 10000) {
                log.warn("本文のサイズが大きすぎます。最大10000byte");
                throw new IOException("Too Big Message Body. Max 10000 byte.");
            }
            multi.addPart("folder.mail.data", new StringBody(body, Charset.forName("UTF-8")));
            if (!attachmentFileIdList.isEmpty()) {
                for (int i = 0; i < attachmentFileIdList.size(); i++) {
                    multi.addPart("folder.tmpfile(" + i + ").file(0).id", new StringBody(attachmentFileIdList.get(i), Charset.forName("UTF-8")));
                }
            }
            multi.addPart("iemoji(0).id", new StringBody(Character.toString((char) 0xe709), Charset.forName("UTF-8")));
            multi.addPart("iemoji(1).id", new StringBody(Character.toString((char) 0xe6f0), Charset.forName("UTF-8")));
            multi.addPart("reqtype", new StringBody("0", Charset.forName("UTF-8")));
            HttpPost post = new HttpPost(SendMailUrl);
            try {
                addDumyHeader(post);
                post.setEntity(multi);
                HttpResponse res = this.executeHttp(post);
                if (!isJson(res)) {
                    log.warn("応答がJSON形式ではありません。");
                    if (res != null) {
                        log.debug(toStringBody(res));
                        this.logined = Boolean.FALSE;
                        throw new LoginException("Bad response. no json format.");
                    } else {
                        throw new IOException("imode.net not responding. Try later.");
                    }
                }
                JSONObject json = JSONObject.fromObject(toStringBody(res));
                String result = json.getJSONObject("common").getString("result");
                if (result.equals("PW1409")) {
                    this.logined = Boolean.FALSE;
                    throw new IOException("PW1409 - session terminated because of your bad mail.");
                } else if (result.equals("PW1430")) {
                    throw new IOException("PW1430 - User Unknown.");
                } else if (result.equals("PW1436")) {
                    JSONArray jsonaddrs = json.getJSONObject("data").getJSONArray("seaddr");
                    String addrs = "";
                    for (int i = 0; i < jsonaddrs.size(); i++) {
                        if (i > 0) {
                            addrs += ", ";
                        }
                        addrs += jsonaddrs.getString(i);
                    }
                    throw new IOException("PW1436 - User Unknown.: " + addrs);
                } else if (!result.equals("PW1000")) {
                    log.debug(json.toString(2));
                    throw new IOException("Bad response " + result);
                }
            } finally {
                post.abort();
                log.info("メール送信処理終了");
            }
        } catch (UnsupportedEncodingException e) {
            log.fatal(e);
        }
    }
}
