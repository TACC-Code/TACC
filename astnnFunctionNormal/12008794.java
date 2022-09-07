class BackupThread extends Thread {
    public final void testUpdate() {
        Broadcast updatedData = null;
        try {
            updatedData = broadcastDao.getById(new Long(3));
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        updatedData.setName("newName");
        updatedData.setExtId("newExt");
        updatedData.setDescr("newDescr");
        updatedData.setExtId("newExt");
        updatedData.setName("newName");
        updatedData.setPicName("newPic");
        updatedData.setType(new Long(200));
        updatedData.setChannel(getChannel(new Long(3), channelDao));
        try {
            broadcastDao.update(updatedData);
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        Broadcast testData = null;
        try {
            testData = broadcastDao.getById(updatedData.getId());
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        assertNotNull("null", testData);
        assertEquals(updatedData, testData);
    }
}
