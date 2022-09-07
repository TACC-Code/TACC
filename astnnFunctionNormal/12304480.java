class BackupThread extends Thread {
    public static void createImageFromPDFFailed(File pdfFile, File pngFile) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        int num = pdffile.getNumPages();
        for (int i = 0; i < num; i++) {
            PDFPage page = pdffile.getPage(i);
            int width = (int) page.getBBox().getWidth();
            int height = (int) page.getBBox().getHeight();
            java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, width, height);
            BufferedImage img = (BufferedImage) page.getImage((int) rect.getWidth(), (int) rect.getHeight(), rect, null, true, true);
            ImageIO.write(img, "png", pngFile);
        }
    }
}
