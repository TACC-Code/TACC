class BackupThread extends Thread {
    public final void testSave() {
        Broadcast savedData = new Broadcast();
        Long id = null;
        savedData.setName("newName");
        savedData.setExtId("newExt");
        savedData.setDescr("newDescr");
        savedData.setExtId("newExt");
        savedData.setName("newName");
        savedData.setPicName("newPic");
        savedData.setType(new Long(200));
        savedData.setChannel(getChannel(new Long(3), channelDao));
        try {
            id = broadcastDao.save(savedData);
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        Broadcast testData = null;
        try {
            testData = broadcastDao.getById(id);
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        assertNotNull("null", testData);
        assertEquals(savedData, testData);
    }
}
