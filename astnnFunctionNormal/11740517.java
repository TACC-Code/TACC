class BackupThread extends Thread {
    @Override
    public void run() {
        System.gc();
        byte[] md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5").digest(b);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Enregistrement.class.getName()).log(Level.SEVERE, null, ex);
        }
        MD5 = "";
        for (int i = 0; i < md5.length; i++) {
            MD5 = MD5 + md5[i];
        }
        id_code = Donnees.rechercherDonnee(b, MD5);
        new Constituer(id_fichier, id_code, sit);
        try {
            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(Enregistrement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
