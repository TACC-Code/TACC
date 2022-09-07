class BackupThread extends Thread {
    private String createShapeFile(String fileName, FeatureSource layer) throws IOException, Exception {
        FeatureCollection featureCollection = layer.getFeatures();
        File shapeFile = new File(fileName);
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", shapeFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.createSchema((SimpleFeatureType) layer.getSchema());
        newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
        Transaction transaction = new DefaultTransaction("create");
        String typeName = newDataStore.getTypeNames()[0];
        FeatureSource shapeSource = newDataStore.getFeatureSource(typeName);
        if (shapeSource instanceof FeatureStore) {
            FeatureStore shapeStore = (FeatureStore) shapeSource;
            shapeStore.setTransaction(transaction);
            try {
                shapeStore.addFeatures(featureCollection);
                transaction.commit();
            } catch (Exception e) {
                logger.severe("Problem writing shapefile: " + e.getMessage());
                transaction.rollback();
            } finally {
                transaction.close();
            }
        } else {
            throw new Exception(typeName + " does not support read/write access");
        }
        return shapeFile.getAbsolutePath().replace(".shp", "");
    }
}
