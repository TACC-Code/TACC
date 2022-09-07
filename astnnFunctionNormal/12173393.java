class BackupThread extends Thread {
    @BeforeClass
    @AfterClass
    public static void cleanUpDatabase() {
        try {
            if (EntityManagerHelper.getTransaction().isActive()) {
                EntityManagerHelper.rollback();
            }
        } catch (final PersistenceException exception) {
            ;
        }
        EntityManagerHelper.beginTransaction();
        final Query removeElementsQuery = EntityManagerHelper.createQuery("DELETE from Element e where e.id >= 200");
        removeElementsQuery.executeUpdate();
        final Query removePlanetsQuery = EntityManagerHelper.createQuery("DELETE from Planet p where p.id > 9");
        removePlanetsQuery.executeUpdate();
        EntityManagerHelper.createQuery("DELETE from Scientist s").executeUpdate();
        EntityManagerHelper.createQuery("DELETE from NobelPrize n").executeUpdate();
        EntityManagerHelper.createQuery("DELETE from NobelPrizeLaureate n").executeUpdate();
        EntityManagerHelper.commit();
    }
}
