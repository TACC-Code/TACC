class BackupThread extends Thread {
    public void testCreateRecipient() throws Exception {
        Address address = factory.createMSISDN("07777123456789");
        Recipient recipient = factory.createRecipient(address, "Nokia-6600");
        assertNotNull(recipient);
        assertEquals("07777123456789", recipient.getAddress().getValue());
        assertEquals("Default recipient Type is TO", RecipientType.TO, recipient.getRecipientType());
        assertEquals("Nokia-6600", recipient.getDeviceName());
        assertNull("Default Channel is null", recipient.getChannel());
    }
}
