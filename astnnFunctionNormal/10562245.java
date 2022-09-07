class BackupThread extends Thread {
    @Test
    public void parseAngelHuman() {
        ScienceParserData data = ScienceParser.parse(fileToString("/age41/angel/combo/human-merchant-sos.txt"));
        assertNotNull(data);
        assertNotNull(data.getProvinceName());
        assertEquals(11, data.getCoordinate().getKingdom());
        assertEquals(11, data.getCoordinate().getIsland());
        data.adjustScience(550, Race.Human);
        Science science = data.getScience();
        assertTrue(data.isRaw());
        assertEquals(39871, science.getAlchemy());
        assertEquals(63205, science.getTools());
        assertEquals(68991, science.getHousing());
        assertEquals(7942, science.getFood());
        assertEquals(14225, science.getMilitary());
        assertEquals(5726, science.getCrime());
        assertEquals(2926, science.getChanneling());
    }
}
