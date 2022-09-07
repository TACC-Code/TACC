class BackupThread extends Thread {
    public static void descarga(String origen, String destino) {
        log.debug("comenzando la descarga");
        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            url = new URL(origen);
            con = url.openConnection();
            dis = new DataInputStream(con.getInputStream());
            fileData = new byte[con.getContentLength()];
            for (int x = 0; x < fileData.length; x++) {
                fileData[x] = dis.readByte();
            }
            dis.close();
            fos = new FileOutputStream(new File(destino));
            fos.write(fileData);
            fos.close();
        } catch (MalformedURLException m) {
            System.out.println(m);
        } catch (IOException io) {
            System.out.println(io);
        }
        log.debug("Descarga finalizada");
    }
}
