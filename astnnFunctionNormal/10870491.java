class BackupThread extends Thread {
    public void testClone() throws MalformedURLException, MessageException {
        String messageText = "Message Text 1";
        URL url = new URL("http://www.volantis.com/1");
        String subject = "Subject 1";
        String headerAllName = "HeaderAll1";
        String headerAllValue = "AllValue1";
        String headerMMSName = "HeaderMMS1";
        String headerMMSValue = "value1";
        String headerMHTMLName = "HeaderMHTML1";
        String headerMHTMLValue = "value1";
        MessageAttachments attachments = new MessageAttachments();
        DeviceMessageAttachment attachment = new DeviceMessageAttachment();
        String attachmentChannel = "SMTP";
        String attachmentDevice = "Outlook";
        String attachmentMimeType = "text/plain";
        String attachmentValue = LOCAL_FILE_URLS[0];
        MultiChannelMessageImpl message = new MultiChannelMessageImpl();
        message.setMessage(messageText);
        message.setMessageURL(url);
        message.setSubject(subject);
        message.addHeader(MultiChannelMessage.ALL, headerAllName, headerAllValue);
        message.addHeader(MultiChannelMessage.MMS, headerMMSName, headerMMSValue);
        message.addHeader(MultiChannelMessage.MHTML, headerMHTMLName, headerMHTMLValue);
        attachment.setChannelName(attachmentChannel);
        attachment.setDeviceName(attachmentDevice);
        attachment.setMimeType(attachmentMimeType);
        attachment.setValue(attachmentValue);
        attachment.setValueType(DeviceMessageAttachment.URL);
        attachments.addAttachment(attachment);
        message.addAttachments(attachments);
        MultiChannelMessage clone = (MultiChannelMessage) message.clone();
        message.removeAttachments();
        message.setMessage(null);
        message.setMessageURL(null);
        message.setSubject(null);
        message = null;
        assertEquals(clone.getMessage(), messageText);
        assertEquals(clone.getMessageURL(), url);
        assertEquals(clone.getSubject(), subject);
        MessageAttachments retrievedMessageAttachments = clone.getAttachments();
        Iterator i = retrievedMessageAttachments.iterator();
        int count = 0;
        while (i.hasNext()) {
            count++;
            DeviceMessageAttachment retrievedAttachment = (DeviceMessageAttachment) i.next();
            assertEquals(retrievedAttachment.getChannelName(), attachmentChannel);
            assertEquals(retrievedAttachment.getDeviceName(), attachmentDevice);
            assertEquals(retrievedAttachment.getMimeType(), attachmentMimeType);
            assertEquals(retrievedAttachment.getValue(), attachmentValue);
        }
        if (count != 1) {
            fail("Expected one attachment got " + count);
        }
        Map headersAll = clone.getHeaders(MultiChannelMessage.ALL);
        Map headersMHTML = clone.getHeaders(MultiChannelMessage.MHTML);
        Map headersMMS = clone.getHeaders(MultiChannelMessage.MMS);
        i = headersAll.keySet().iterator();
        count = 0;
        while (i.hasNext()) {
            count++;
            String key = (String) i.next();
            String value = (String) headersAll.get(key);
            assertEquals(key, headerAllName);
            assertEquals(value, headerAllValue);
        }
        if (count != 1) {
            fail("Expected one header of type ALL got " + count);
        }
        i = headersMMS.keySet().iterator();
        count = 0;
        while (i.hasNext()) {
            count++;
            String key = (String) i.next();
            String value = (String) headersMMS.get(key);
            assertEquals(key, headerMMSName);
            assertEquals(value, headerMMSValue);
        }
        if (count != 1) {
            fail("Expected one header of type MMS got " + count);
        }
        i = headersMHTML.keySet().iterator();
        count = 0;
        while (i.hasNext()) {
            count++;
            String key = (String) i.next();
            String value = (String) headersMHTML.get(key);
            assertEquals(key, headerMHTMLName);
            assertEquals(value, headerMHTMLValue);
        }
        if (count != 1) {
            fail("Expected one header of type MHTML got " + count);
        }
    }
}
