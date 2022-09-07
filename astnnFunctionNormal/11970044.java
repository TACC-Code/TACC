class BackupThread extends Thread {
    private String writeUsingSMTP(MessageContext msgContext) throws Exception {
        String id = (new java.rmi.server.UID()).toString();
        String smtpHost = msgContext.getStrProp(MailConstants.SMTP_HOST);
        SMTPClient client = new SMTPClient();
        client.connect(smtpHost);
        System.out.print(client.getReplyString());
        int reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            AxisFault fault = new AxisFault("SMTP", "( SMTP server refused connection )", null, null);
            throw fault;
        }
        client.login(smtpHost);
        System.out.print(client.getReplyString());
        reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            AxisFault fault = new AxisFault("SMTP", "( SMTP server refused connection )", null, null);
            throw fault;
        }
        String fromAddress = msgContext.getStrProp(MailConstants.FROM_ADDRESS);
        String toAddress = msgContext.getStrProp(MailConstants.TO_ADDRESS);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromAddress));
        msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toAddress));
        String action = msgContext.useSOAPAction() ? msgContext.getSOAPActionURI() : "";
        if (action == null) {
            action = "";
        }
        Message reqMessage = msgContext.getRequestMessage();
        msg.addHeader(HTTPConstants.HEADER_USER_AGENT, Messages.getMessage("axisUserAgent"));
        msg.addHeader(HTTPConstants.HEADER_SOAP_ACTION, action);
        msg.setDisposition(MimePart.INLINE);
        msg.setSubject(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream(8 * 1024);
        reqMessage.writeTo(out);
        msg.setContent(out.toString(), reqMessage.getContentType(msgContext.getSOAPConstants()));
        ByteArrayOutputStream out2 = new ByteArrayOutputStream(8 * 1024);
        msg.writeTo(out2);
        client.setSender(fromAddress);
        System.out.print(client.getReplyString());
        client.addRecipient(toAddress);
        System.out.print(client.getReplyString());
        Writer writer = client.sendMessageData();
        System.out.print(client.getReplyString());
        writer.write(out2.toString());
        writer.flush();
        writer.close();
        System.out.print(client.getReplyString());
        if (!client.completePendingCommand()) {
            System.out.print(client.getReplyString());
            AxisFault fault = new AxisFault("SMTP", "( Failed to send email )", null, null);
            throw fault;
        }
        System.out.print(client.getReplyString());
        client.logout();
        client.disconnect();
        return id;
    }
}
