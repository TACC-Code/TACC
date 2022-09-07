class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public void run() {
        int messagesProcessed = 0;
        NanoTimer timer = NanoTimers.startOn("Pop3EmailActivity");
        try {
            Properties props = new Properties();
            String protocol = null;
            String ssl = activity.getPrefValue(AbstractEmailActivity.SSL_PREF);
            if (ssl.equals(XmlConstantsV10c.TRUE_VALUE)) protocol = AbstractEmailActivity.PROTOCOL_POP3S; else protocol = AbstractEmailActivity.PROTOCOL_POP3;
            final String host = activity.getPrefValue(AbstractEmailActivity.EMAIL_HOST_PREF);
            final String port = activity.getPrefValue(AbstractEmailActivity.EMAIL_PORT_PREF);
            String userName = activity.getPrefValue(AbstractEmailActivity.EMAIL_UID_PREF);
            final String password = activity.getPrefValue(AbstractEmailActivity.EMAIL_PASSWORD_PREF);
            final String emailFolder = activity.getPrefValue(AbstractEmailActivity.EMAIL_FOLDER_PREF);
            final int noOfMessages = Integer.parseInt(activity.getPrefValue(AbstractEmailActivity.EMAIL_NO_OF_MSGS_PREF));
            if (host.indexOf("gmail.com") != -1) userName = "recent:" + userName;
            props.put("mail." + protocol + ".host", host);
            props.put("mail." + protocol + ".port", port);
            props.put("mail.store.protocol", protocol);
            props.put("mail.transport.protocol", protocol);
            Authenticator auth = null;
            props.put("mail.user", userName);
            props.put("mail.password", password);
            UserDocument document = user.getUserDocument(locator.documentFqn);
            activity.createTempFolder(user, locator);
            String uuidHref = activity.createUuidDoc(user, locator);
            UserDocument uuidDocument = user.getUserDocument(uuidHref);
            activity.setPrefValue(AbstractEmailActivity.INIT_DOC_PREF, XmlConstantsV10c.FALSE_VALUE);
            XStream xstream = new XStream();
            HashSet<String> newSet = new HashSet<String>();
            boolean hasChanged = false;
            String oldXML = uuidDocument.getContentAsString();
            HashSet<String> oldSet = (HashSet<String>) xstream.fromXML(oldXML);
            Session session = Session.getInstance(props, auth);
            Store store = session.getStore(protocol);
            store.connect(host, userName, password);
            NanoTimers.step(timer, "getFolder");
            POP3Folder folder = (POP3Folder) store.getFolder(emailFolder);
            folder.open(Folder.READ_WRITE);
            FetchProfile profile = new FetchProfile();
            profile.add(UIDFolder.FetchProfileItem.UID);
            Message[] messages = folder.getMessages();
            folder.fetch(messages, profile);
            NanoTimers.step(timer, "search");
            NanoTimers.step(timer, "getUserDocument");
            ArrayList<String> records = new ArrayList<String>();
            for (int i = messages.length - 1; i >= 0; i--) {
                Message currentMessage = messages[i];
                String messageId = folder.getUID(messages[i]);
                newSet.add(messageId);
                if (oldSet.contains(messageId)) continue;
                hasChanged = true;
                Address[] fromAddress;
                String from = "";
                if ((fromAddress = currentMessage.getFrom()) != null) {
                    for (int j = 0; j < fromAddress.length; j++) from += fromAddress[j].toString();
                }
                String to = getRecipientList(currentMessage, Message.RecipientType.TO);
                String cc = getRecipientList(currentMessage, Message.RecipientType.CC);
                String bcc = getRecipientList(currentMessage, Message.RecipientType.BCC);
                MFEMail xw = new MFEMail();
                xw.setRead(false);
                xw.setStarred(false);
                xw.setMessageId(messageId);
                xw.setFrom(from);
                xw.setTo(to);
                xw.setCc(cc);
                xw.setBcc(bcc);
                xw.setSubject(currentMessage.getSubject());
                String content = "";
                if (currentMessage.isMimeType("text/plain") || currentMessage.isMimeType("text/html")) {
                    try {
                        content = (String) currentMessage.getContent();
                    } catch (Exception e) {
                        content = "Could not retrieve content of message: " + e;
                    }
                }
                NanoTimers.step(timer, "getContent");
                if (currentMessage.isMimeType("multipart/alternative")) content = MailUtil.parseMailPart(currentMessage); else if (currentMessage.isMimeType("multipart/*")) {
                    MimeMultipart multipart = (MimeMultipart) currentMessage.getContent();
                    for (int k = 0; k < multipart.getCount(); k++) {
                        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(k);
                        String disposition = part.getDisposition();
                        if (disposition == null) {
                            content = MailUtil.parseMailPart(part);
                        } else if (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE)) {
                            int fileSize = part.getSize() / 1024;
                            String fileName = part.getFileName();
                            String fileContent = part.getContentType();
                            if (fileSize == 0) fileSize = 1;
                            if (fileName == null || fileName.equals(null) || fileName.equals("")) {
                                content += "\n\n" + MailUtil.parseMailPart(part);
                            } else {
                                InputStream istream = part.getInputStream();
                                String localFilename = messageId + "_old_" + k;
                                String base = locator.baseFolderFqn;
                                String href = base + localFilename;
                                try {
                                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[4000];
                                    int length;
                                    while ((length = istream.read(buffer)) >= 0) bout.write(buffer, 0, length);
                                    bout.flush();
                                    buffer = bout.toByteArray();
                                    NanoTimers.step(timer, "getAttachment");
                                    user.putFile(href, buffer, true);
                                    NanoTimers.step(timer, "putFile");
                                    bout.close();
                                } catch (UnsupportedEncodingException e) {
                                    Loggers.XML_RUNTIME.error("Unsupported Encoding", e);
                                } catch (IOException e) {
                                    Loggers.XML_RUNTIME.error("Error in writing ByteArrayOutputStream", e);
                                }
                                xw.addAttachment(href, fileContent.substring(0, fileContent.indexOf(";")), fileSize + "K", fileName);
                            }
                        }
                    }
                }
                NanoTimers.step(timer, "getAttachment");
                xw.setMessage(content);
                String textContent = "";
                if (content != null) textContent = MailUtil.convertHtml2Text(content);
                if (textContent.equals("")) textContent = content;
                xw.setTextContent(textContent);
                xw.setSentDate(currentMessage.getSentDate());
                records.add(xw.toXMLString());
                messagesProcessed++;
                NanoTimers.step(timer, "getContent");
                if (noOfMessages > 0 && messagesProcessed >= noOfMessages) break;
            }
            if (hasChanged) {
                String newXML = xstream.toXML(newSet);
                uuidDocument.setContent(newXML);
            }
            if (records.size() > 0) {
                String[] recordArray = new String[records.size()];
                for (int i = 0; i < recordArray.length; i++) recordArray[recordArray.length - i - 1] = records.get(i);
                document.insertRecords("/tq:vector/tq:focussed", recordArray);
                NanoTimers.step(timer, "insertRecord");
            }
            folder.close(false);
            store.close();
            NanoTimers.step(timer, "close");
        } catch (AuthenticationFailedException e) {
            output.errorMessage = "Authentication failed. Please check username and password.";
            output.exception = e;
        } catch (MessageRemovedException e) {
            output.errorMessage = "Message was removed from server. Please retry.";
            output.exception = e;
        } catch (TeqloException e) {
            output.errorMessage = "Error updating Email document content in database.";
            output.exception = e;
        } catch (Exception e) {
            output.errorMessage = "Error retrieving POP mail: %1$s.";
            output.exception = e;
        } finally {
            try {
                user.removeAttribute(lockName);
            } catch (TeqloException e) {
            }
        }
        try {
            String resultActivity = activity.getAttributeValue(AbstractEmailActivity.RESULT_ACTIVITY);
            if (resultActivity != null) if (resultActivity.length() == 0) resultActivity = null; else resultActivity = References.asFolder(References.extractContainingFolder(activity.getActivityLookup().getActivityFqn())) + resultActivity;
            if (resultActivity != null) XmlRuntime.getInstance().invokeServiceSync(user, activity.getActivityLookup().getExecutorLookup().getServiceLookup().getServiceFqn(), resultActivity, output); else throw new TeqloException(this, AbstractEmailActivity.RESULT_ACTIVITY, null, "attribute must be set");
        } catch (TeqloException e) {
            Loggers.XML_RUNTIME.error("Could not process results of mail fetch operation", e);
        }
        NanoTimers.step(timer, "actionsOnRun", messagesProcessed);
    }
}
