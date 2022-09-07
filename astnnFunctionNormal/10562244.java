class BackupThread extends Thread {
    @Test
    public void parseAngelSelfElf() {
        ScienceParserData data = ScienceParser.parse(fileToString("/age41/angel/self/elf-warhero-sos.txt"));
        assertNotNull(data);
        assertNotNull(data.getProvinceName());
        assertEquals(12, data.getCoordinate().getKingdom());
        assertEquals(3, data.getCoordinate().getIsland());
        data.adjustScience(1017, Race.Human);
        Science science = data.getScience();
        assertFalse(data.isRaw());
        assertEquals(18852, science.getAlchemy());
        assertEquals(18128, science.getTools());
        assertEquals(17759, science.getHousing());
        assertEquals(6439, science.getFood());
        assertEquals(1242, science.getMilitary());
        assertEquals(10318, science.getCrime());
        assertEquals(10429, science.getChanneling());
    }
}
