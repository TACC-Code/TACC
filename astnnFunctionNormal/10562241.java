class BackupThread extends Thread {
    @Test
    public void checkSelfScience() {
        ScienceParserData data = ScienceParser.parse(fileToString("/age41/self/dwarf-sage-sos.txt"));
        assertNotNull(data);
        assertNull(data.getProvinceName());
        data.adjustScience(550, Race.Dwarf);
        Science science = data.getScience();
        assertEquals(8068, science.getAlchemy());
        assertEquals(8063, science.getTools());
        assertEquals(7558, science.getHousing());
        assertEquals(3127, science.getFood());
        assertEquals(4056, science.getMilitary());
        assertEquals(5210, science.getCrime());
        assertEquals(5207, science.getChanneling());
    }
}
