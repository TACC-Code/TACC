class BackupThread extends Thread {
    private void downloadFile(String filePath, String destination) {
        URLConnection connection = null;
        InputStream is = null;
        FileOutputStream destinationFile = null;
        try {
            URL url = new URL(filePath);
            connection = url.openConnection();
            int length = connection.getContentLength();
            if (length == -1) {
                throw new IOException("Fichier vide");
            }
            is = new BufferedInputStream(connection.getInputStream());
            byte[] data = new byte[length];
            int currentBit = 0;
            int deplacement = 0;
            while (deplacement < length) {
                currentBit = is.read(data, deplacement, data.length - deplacement);
                if (currentBit == -1) break;
                deplacement += currentBit;
            }
            if (deplacement != length) {
                throw new IOException("Le fichier n'a pas �t� lu en entier (seulement " + deplacement + " sur " + length + ")");
            }
            destinationFile = new FileOutputStream(destination);
            destinationFile.write(data);
            destinationFile.flush();
        } catch (MalformedURLException e) {
            System.err.println("Probl�me avec l'URL : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                destinationFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
