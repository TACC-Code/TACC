class BackupThread extends Thread {
    public void downloadImage(String adresse, String nom, Dossier pere) {
        try {
            URL url = new URL(adresse);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            File photo = new File("receptionFlickr/" + nom + ".jpg");
            FileOutputStream fo = new FileOutputStream(photo);
            byte[] buffer = new byte[4096];
            for (int read = 0; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) ;
            fo.write(out.toByteArray());
            fo.close();
            JPEG nouv = new JPEG(pere, photo, nom);
            int emplacement = pere.getIndex(nouv);
            pere.addChild(nouv, emplacement);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
