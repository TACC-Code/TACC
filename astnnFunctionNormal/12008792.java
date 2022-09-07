class BackupThread extends Thread {
    public final void testGetById() {
        Broadcast testData = null;
        Long id = new Long(1);
        try {
            testData = broadcastDao.getById(id);
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        assertNotNull("null", testData);
        assertEquals("ID", testData.getId(), id);
        assertEquals("date", testData.getDate().getTime(), new Date(106, 9, 9, 15, 00, 10).getTime());
        assertEquals("descr", testData.getDescr(), "descr1");
        assertEquals("extId", testData.getExtId(), "ext1");
        assertEquals("name", testData.getName(), "name1");
        assertEquals("pic", testData.getPicName(), "pic1");
        assertEquals("type", testData.getType(), new Long(100));
        assertEquals("channel", testData.getChannel().getId(), new Long(1));
        testData = null;
        try {
            testData = broadcastDao.getById(new Long(100));
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        assertNull("not null", testData);
    }
}
