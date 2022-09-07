class BackupThread extends Thread {
    public void getFile(String s, String dir) {
        try {
            URI uri = new URI(s);
            URL url = uri.toURL();
            URLConnection urlcon = url.openConnection();
            InputStream fileIS = urlcon.getInputStream();
            String filenamesplit[] = s.split("/");
            File saveFile = new File(dir, filenamesplit[filenamesplit.length - 1]);
            FileOutputStream fileOS = new FileOutputStream(saveFile);
            int c;
            while ((c = fileIS.read()) != -1) {
                fileOS.write((byte) c);
            }
            System.out.println("Get " + filenamesplit[filenamesplit.length - 1]);
            fileOS.close();
            fileIS.close();
        } catch (MalformedURLException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } catch (URISyntaxException e) {
            System.err.println(e);
        }
    }
}
