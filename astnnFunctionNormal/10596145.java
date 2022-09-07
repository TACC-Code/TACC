class BackupThread extends Thread {
    public static ImageAdapter getImageForURL(String imgUrl, boolean saveInGroIMP) {
        ImageAdapter ia = null;
        boolean loadedFromWeb = false;
        File f = null;
        URL url = null;
        Workbench wb = Workbench.current();
        try {
            String imgpath = imgUrl;
            if (imgpath.startsWith("\"") && imgpath.endsWith("\"")) imgpath = imgpath.substring(1, imgpath.length() - 1);
            if (imgpath.toLowerCase().startsWith("http://")) {
                String filename = imgpath.substring(imgpath.lastIndexOf("/") + 1, imgpath.lastIndexOf("."));
                String fileext = imgpath.substring(imgpath.lastIndexOf("."), imgpath.length());
                f = File.createTempFile(filename, fileext);
                url = new URL(imgpath);
                InputStream is = url.openStream();
                FileOutputStream os = new FileOutputStream(f);
                byte[] buffer = new byte[0xFFFF];
                for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
                is.close();
                os.close();
                url = f.toURI().toURL();
                loadedFromWeb = true;
            } else {
                if (imgpath.startsWith("/") || (imgpath.charAt(1) == ':')) {
                } else {
                    File x3dfile = X3DImport.getTheImport().getCurrentParser().getFile();
                    imgpath = Util.getRealPath(x3dfile) + imgpath;
                }
                f = new File(imgpath);
                url = f.toURI().toURL();
                Object testContent = url.getContent();
                if (testContent == null) return null;
                loadedFromWeb = false;
            }
            if (saveInGroIMP) {
                FileFactory ff = ImageReader.getFactory(wb.getRegistry());
                ia = (FixedImageAdapter) ff.addFromURL(wb.getRegistry(), url, null, wb);
            } else {
                ia = new FixedImageAdapter(ImageIO.read(url));
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (loadedFromWeb && f != null) {
                f.delete();
            }
        }
        return ia;
    }
}
