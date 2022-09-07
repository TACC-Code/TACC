class BackupThread extends Thread {
    void load(File f, float detailFactor) {
        try {
            System.out.println("Loading file...");
            if (f.getName().toLowerCase().endsWith(".pdf")) {
                RandomAccessFile raf = new RandomAccessFile(f, "r");
                FileChannel channel = raf.getChannel();
                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                PDFFile pdfFile = new PDFFile(buf);
                int page_width = (int) pdfFile.getPage(0).getBBox().getWidth();
                System.out.println("page width " + page_width);
                for (int i = 0; i < pdfFile.getNumPages(); i++) {
                    try {
                        ZPDFPageImg p = new ZPDFPageImg(i * Math.round(page_width * 1.1f * detailFactor), i * Math.round(page_width * 1.1f * detailFactor), 0, pdfFile.getPage(i + 1), detailFactor, 1);
                        p.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        vs.addGlyph(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (f.getName().toLowerCase().endsWith(".png12")) {
                VImage im = new VImage(0, 0, 0, (new ImageIcon("images/1-12/map5/H/map5_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(-2000, 0, 0, (new ImageIcon("images/1-12/map4/H/map4_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(2000, 0, 0, (new ImageIcon("images/1-12/map6/H/map6_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(0, 2000, 0, (new ImageIcon("images/1-12/map2/H/map2_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(-2000, 2000, 0, (new ImageIcon("images/1-12/map1/H/map1_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(2000, 2000, 0, (new ImageIcon("images/1-12/map3/H/map3_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(0, -2000, 0, (new ImageIcon("images/1-12/map8/H/map8_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(-2000, -2000, 0, (new ImageIcon("images/1-12/map7/H/map7_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(2000, -2000, 0, (new ImageIcon("images/1-12/map9/H/map9_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(-4000, 2000, 0, (new ImageIcon("images/1-12/mapA/H/mapA_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(-4000, 0, 0, (new ImageIcon("images/1-12/mapB/H/mapB_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(-4000, -2000, 0, (new ImageIcon("images/1-12/mapC/H/mapC_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(4000, 2000, 0, (new ImageIcon("images/1-12/mapK/H/mapK_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(4000, 0, 0, (new ImageIcon("images/1-12/mapJ/H/mapJ_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
                im = new VImage(4000, -2000, 0, (new ImageIcon("images/1-12/mapI/H/mapI_p1.png")).getImage(), 12.4);
                vs.addGlyph(im);
            } else {
                VImage im = new VImage(0, 0, 0, (new ImageIcon("images/1/map5/H/map5_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(-2000, 0, 0, (new ImageIcon("images/1/map4/H/map4_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(2000, 0, 0, (new ImageIcon("images/1/map6/H/map6_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(0, 2000, 0, (new ImageIcon("images/1/map2/H/map2_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(-2000, 2000, 0, (new ImageIcon("images/1/map1/H/map1_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(2000, 2000, 0, (new ImageIcon("images/1/map3/H/map3_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(0, -2000, 0, (new ImageIcon("images/1/map8/H/map8_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(-2000, -2000, 0, (new ImageIcon("images/1/map7/H/map7_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(2000, -2000, 0, (new ImageIcon("images/1/map9/H/map9_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(-4000, 2000, 0, (new ImageIcon("images/1/mapA/H/mapA_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(-4000, 0, 0, (new ImageIcon("images/1/mapB/H/mapB_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(-4000, -2000, 0, (new ImageIcon("images/1/mapC/H/mapC_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(4000, 2000, 0, (new ImageIcon("images/1/mapK/H/mapK_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(4000, 0, 0, (new ImageIcon("images/1/mapJ/H/mapJ_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
                im = new VImage(4000, -2000, 0, (new ImageIcon("images/1/mapI/H/mapI_p1.png")).getImage());
                im.setInterpolationMethod(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                vs.addGlyph(im);
            }
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
