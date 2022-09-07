class BackupThread extends Thread {
    public void createPicture(File destPath, String fileName, String text, String color) {
        try {
            text = Translate.decode(text);
            text = java.net.URLEncoder.encode(text, "UTF-8");
            if (destPath == null) destPath = new File(".");
            if (destPath != null) destPath.mkdirs();
            if (new File(destPath, fileName).exists()) System.out.println("WARNING FILE EXISTS: " + destPath + File.separator + fileName);
            String userProfileDir = (System.getenv().get("APPDATA") != null) ? System.getenv().get("APPDATA") : ".";
            File userSettingsDir = new File(userProfileDir, "fma" + File.separator + "imageGenerator");
            userSettingsDir.mkdirs();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(destPath, fileName)), 1024 * 250);
            BufferedOutputStream out_control = new BufferedOutputStream(new FileOutputStream(new File(userSettingsDir, destPath.getPath().replace(File.separatorChar, '_') + "_" + fileName)), 1024 * 250);
            URL url = new URL("http://www.porsche.com/ImageMachines/CCTitles.aspx/" + fileName + "?text=" + text + "&mode=" + color);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("ISO-8859-1")));
            int inputLine;
            while ((inputLine = in.read()) != -1) {
                out.write(inputLine);
                out_control.write(inputLine);
            }
            out.flush();
            out_control.flush();
            out.close();
            out_control.close();
            out = null;
            out_control = null;
            in = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
