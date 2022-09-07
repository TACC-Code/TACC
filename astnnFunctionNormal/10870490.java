class BackupThread extends Thread {
    public void testGetAttachments() throws MessageException {
        MultiChannelMessage message = new MultiChannelMessageImpl();
        MessageAttachments attachments = new MessageAttachments();
        DeviceMessageAttachment attachment = new DeviceMessageAttachment();
        String attachmentChannel = "SMTP";
        String attachmentDevice = "Outlook";
        String attachmentMimeType = "text/plain";
        String attachmentValue = LOCAL_FILE_URLS[0];
        attachment.setChannelName(attachmentChannel);
        attachment.setDeviceName(attachmentDevice);
        attachment.setMimeType(attachmentMimeType);
        attachment.setValue(attachmentValue);
        attachment.setValueType(DeviceMessageAttachment.URL);
        attachments.addAttachment(attachment);
        message.addAttachments(attachments);
        MessageAttachments retrievedMessageAttachments = message.getAttachments();
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
    }
}
