class BackupThread extends Thread {
    public MapData(final ResourceLoader loader) {
        m_resourceLoader = loader;
        try {
            final String prefix = "";
            m_place = PointFileReaderWriter.readOneToMany(loader.getResourceAsStream(prefix + PLACEMENT_FILE));
            m_territoryEffects = PointFileReaderWriter.readOneToMany(loader.getResourceAsStream(prefix + TERRITORY_EFFECT_FILE));
            m_polys = PointFileReaderWriter.readOneToManyPolygons(loader.getResourceAsStream(prefix + POLYGON_FILE));
            m_centers = PointFileReaderWriter.readOneToOneCenters(loader.getResourceAsStream(prefix + CENTERS_FILE));
            m_vcPlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + VC_MARKERS));
            m_convoyPlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + CONVOY_MARKERS));
            m_blockadePlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + BLOCKADE_MARKERS));
            m_capitolPlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + CAPITAL_MARKERS));
            m_PUPlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + PU_PLACE_FILE));
            m_namePlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + TERRITORY_NAME_PLACE_FILE));
            m_kamikazePlace = PointFileReaderWriter.readOneToOne(loader.getResourceAsStream(prefix + KAMIKAZE_FILE));
            m_mapProperties = new Properties();
            loadDecorations();
            try {
                final URL url = loader.getResource(prefix + MAP_PROPERTIES);
                if (url == null) throw new IllegalStateException("No map.properties file defined");
                m_mapProperties.load(url.openStream());
            } catch (final Exception e) {
                System.out.println("Error reading map.properties:" + e);
            }
            initializeContains();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
