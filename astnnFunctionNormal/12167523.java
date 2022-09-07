class BackupThread extends Thread {
    private static Documento pdf2Documento(String path, String usuario, String rol) {
        File file = new File(path);
        RandomAccessFile raf;
        Documento res = new Documento(usuario, rol);
        try {
            raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);
            int num = pdffile.getNumPages();
            for (int i = 1; i <= num; i++) {
                PDFPage page = pdffile.getPage(i);
                int width = (int) page.getBBox().getWidth();
                int height = (int) page.getBBox().getHeight();
                Rectangle rect = new Rectangle(0, 0, width, height);
                int rotation = page.getRotation();
                Rectangle rect1 = rect;
                if (rotation == 90 || rotation == 270) rect1 = new Rectangle(0, 0, rect.height, rect.width);
                BufferedImage img = (BufferedImage) page.getImage((int) (((float) rect.width * 1.1) + 0.5), (int) (((float) rect.height * 1.1) + 0.5), rect1, null, true, true);
                res.addPagina(new ImageIcon(img));
                System.gc();
            }
            res.setPath(path);
            return res;
        } catch (FileNotFoundException e1) {
            System.err.println(e1.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;
    }
}
