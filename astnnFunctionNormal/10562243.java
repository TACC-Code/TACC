class BackupThread extends Thread {
    @Test
    public void checkOrcAsHuman() {
        ScienceParserData data = ScienceParser.parse(fileToString("/age41/random/orc-warrior-sos.txt"));
        assertNotNull(data);
        assertNotNull(data.getProvinceName());
        assertEquals(13, data.getCoordinate().getKingdom());
        assertEquals(3, data.getCoordinate().getIsland());
        data.adjustScience(311, Race.Human);
        Science science = data.getScience();
        assertEquals(0, science.getAlchemy());
        assertEquals(0, science.getTools());
        assertEquals(0, science.getHousing());
        assertEquals(0, science.getFood());
        assertEquals(2538, science.getMilitary());
        assertEquals(6279, science.getCrime());
        assertEquals(14550, science.getChanneling());
    }
}
