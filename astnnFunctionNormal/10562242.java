class BackupThread extends Thread {
    @Test
    public void checkOrcWarrior() {
        ScienceParserData data = ScienceParser.parse(fileToString("/age41/random/orc-warrior-sos.txt"));
        assertNotNull(data);
        assertNotNull(data.getProvinceName());
        assertEquals(13, data.getCoordinate().getKingdom());
        assertEquals(3, data.getCoordinate().getIsland());
        data.adjustScience(311, Race.Orc);
        Science science = data.getScience();
        assertEquals(0, science.getAlchemy());
        assertEquals(0, science.getTools());
        assertEquals(0, science.getHousing());
        assertEquals(0, science.getFood());
        assertEquals(3966, science.getMilitary());
        assertEquals(9811, science.getCrime());
        assertEquals(22734, science.getChanneling());
    }
}
