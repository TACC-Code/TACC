class BackupThread extends Thread {
    private void readUsingPOP3(String id, MessageContext msgContext) throws Exception {
        String pop3Host = msgContext.getStrProp(MailConstants.POP3_HOST);
        String pop3User = msgContext.getStrProp(MailConstants.POP3_USERID);
        String pop3passwd = msgContext.getStrProp(MailConstants.POP3_PASSWORD);
        Reader reader;
        POP3MessageInfo[] messages = null;
        MimeMessage mimeMsg = null;
        POP3Client pop3 = new POP3Client();
        pop3.setDefaultTimeout(60000);
        for (int i = 0; i < 12; i++) {
            pop3.connect(pop3Host);
            if (!pop3.login(pop3User, pop3passwd)) {
                pop3.disconnect();
                AxisFault fault = new AxisFault("POP3", "( Could not login to server.  Check password. )", null, null);
                throw fault;
            }
            messages = pop3.listMessages();
            if (messages != null && messages.length > 0) {
                StringBuffer buffer = null;
                for (int j = 0; j < messages.length; j++) {
                    reader = pop3.retrieveMessage(messages[j].number);
                    if (reader == null) {
                        AxisFault fault = new AxisFault("POP3", "( Could not retrieve message header. )", null, null);
                        throw fault;
                    }
                    buffer = new StringBuffer();
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    int ch;
                    while ((ch = bufferedReader.read()) != -1) {
                        buffer.append((char) ch);
                    }
                    bufferedReader.close();
                    if (buffer.toString().indexOf(id) != -1) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toString().getBytes());
                        Properties prop = new Properties();
                        Session session = Session.getDefaultInstance(prop, null);
                        mimeMsg = new MimeMessage(session, bais);
                        pop3.deleteMessage(messages[j].number);
                        break;
                    }
                    buffer = null;
                }
            }
            pop3.logout();
            pop3.disconnect();
            if (mimeMsg == null) {
                Thread.sleep(5000);
            } else {
                break;
            }
        }
        if (mimeMsg == null) {
            pop3.logout();
            pop3.disconnect();
            AxisFault fault = new AxisFault("POP3", "( Could not retrieve message list. )", null, null);
            throw fault;
        }
        String contentType = mimeMsg.getContentType();
        String contentLocation = mimeMsg.getContentID();
        Message outMsg = new Message(mimeMsg.getInputStream(), false, contentType, contentLocation);
        outMsg.setMessageType(Message.RESPONSE);
        msgContext.setResponseMessage(outMsg);
        if (log.isDebugEnabled()) {
            log.debug("\n" + Messages.getMessage("xmlRecd00"));
            log.debug("-----------------------------------------------");
            log.debug(outMsg.getSOAPPartAsString());
        }
    }
}
