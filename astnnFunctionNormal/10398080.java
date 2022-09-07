class BackupThread extends Thread {
    @BeforeClass
    public static void initalize() throws Exception {
        File storageDirectory = new File("j:\\neembuu\\heap\\test120k.http.rmvb_neembuu_download_data");
        File[] files = storageDirectory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            file.delete();
        }
        JOptionPane.showMessageDialog(null, "Start");
        try {
            fc1 = new FileInputStream("j:\\neembuu\\virtual\\monitored.nbvfs\\test120k.http.rmvb").getChannel();
        } catch (Exception e) {
            fc1 = null;
        }
        try {
            fc2 = new FileInputStream("j:\\neembuu\\realfiles\\test120k.rmvb").getChannel();
        } catch (Exception e) {
            fc2 = null;
        }
        assertNotNull(fc1);
        assertNotNull(fc2);
        ByteBuffer region1, region11, region2, region3, region4;
    }
}
