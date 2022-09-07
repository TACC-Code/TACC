class BackupThread extends Thread {
    void buildPage(int pagenum, INode pageroot, AffineTransform ctm) throws IOException, ParseException {
        PDFReader pdfr = pdfr_;
        assert pagenum >= 1 && pagenum <= pdfr.getPageCnt() : pagenum + " >= " + pdfr.getPageCnt() + " (1-based)";
        assert pageroot != null;
        if (Multivalent.MONITOR) System.out.print(pagenum + ".  ");
        IRef pageref = pdfr.getPage(pagenum);
        Map page = (Map) pdfr.getObject(pageref);
        Number rotate = (Number) page.get("Rotate");
        int rot = rotate == null ? 0 : rotate.intValue() % 360;
        if (rot < 0) rot += 360;
        assert rot % 90 == 0 && rot >= 0 && rot < 360;
        ctm.rotate(Math.toRadians(-rot));
        Rectangle mediabox = PDFReader.array2Rectangle((Object[]) pdfr.getObject(page.get("MediaBox")), ctm, true);
        cropbox_ = (page.get("CropBox") != null ? PDFReader.array2Rectangle((Object[]) pdfr.getObject(page.get("CropBox")), ctm, true) : mediabox);
        double pw = (double) cropbox_.width, ph = (double) cropbox_.height;
        AffineTransform tmpat = new AffineTransform();
        if (rot == 0) tmpat.setToIdentity(); else if (rot == 90) tmpat.setToTranslation(0.0, ph); else if (rot == 180) tmpat.setToTranslation(pw, ph); else {
            assert rot == 270 : rot;
            tmpat.setToTranslation(pw, 0.0);
        }
        ctm.preConcatenate(tmpat);
        AffineTransform pdf2java = new AffineTransform(1.0, 0.0, 0.0, -1.0, 0.0 - cropbox_.x, ph + (rot == 0 || rot == 180 ? cropbox_.y : -cropbox_.y));
        ctm.preConcatenate(pdf2java);
        cropbox_.setLocation(0, 0);
        ctm_ = new AffineTransform(ctm);
        Object o = page.get("Contents");
        CompositeInputStream in = (o != null ? pdfr.getInputStream(o, true) : null);
        if (Dump_ && in != null) try {
            System.out.println("Contents dict = " + o + "/" + pdfr.getObject(o));
            File tmpf = File.createTempFile("pdf", ".stream");
            tmpf.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tmpf);
            for (int c; (c = in.read()) != -1; ) out.write(c);
            in.close();
            out.close();
            System.out.println("wrote PDF content " + pageref + " to " + tmpf);
            in = pdfr.getInputStream(o, true);
        } catch (IOException ignore) {
            System.err.println("error writing stream: " + ignore);
        }
        List ocrimgs = new ArrayList(10);
        if (in != null) {
            Rectangle clipshape = new Rectangle(cropbox_);
            clipshape.translate(-clipshape.x, -clipshape.y);
            FixedIClip clipp = new FixedIClip("crop", null, pageroot, clipshape, new Rectangle(cropbox_));
            try {
                buildStream(page, clipp, ctm, in, ocrimgs);
            } catch (IOException ioe) {
                throw ioe;
            } catch (Exception pe) {
                pe.printStackTrace();
                throw new ParseException("corrupt content stream: " + pe.toString());
            } finally {
                in.close();
            }
        }
        if (pageroot.getFirstLeaf() == null) {
            pageroot.removeAllChildren();
            new FixedLeafAscii(BLANK_PAGE, null, pageroot);
        }
        assert checkTree("content stream", pageroot);
        OCR.extractBackground(pageroot, this);
        if (pageroot.size() == 0) new FixedLeafAscii("", null, pageroot);
        assert checkTree("bg", pageroot);
        OCR.transform(pageroot, ocrimgs, this);
        assert ocrimgs.size() == 0 || checkTree("OCR", pageroot);
        createAnnots(page, pageroot);
        assert page.get("Annots") == null || checkTree("annos", pageroot);
    }
}
