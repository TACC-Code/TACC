class BackupThread extends Thread {
    public PdfImage(Image image, String name, PdfIndirectReference maskRef) throws BadPdfFormatException {
        super();
        this.name = new PdfName(name);
        put(PdfName.TYPE, PdfName.XOBJECT);
        put(PdfName.SUBTYPE, PdfName.IMAGE);
        put(PdfName.WIDTH, new PdfNumber(image.width()));
        put(PdfName.HEIGHT, new PdfNumber(image.height()));
        if (image.getLayer() != null) put(PdfName.OC, image.getLayer().getRef());
        if (image.isMask() && (image.bpc() == 1 || image.bpc() > 0xff)) put(PdfName.IMAGEMASK, PdfBoolean.PDFTRUE);
        if (maskRef != null) {
            if (image.isSmask()) put(PdfName.SMASK, maskRef); else put(PdfName.MASK, maskRef);
        }
        if (image.isMask() && image.isInvertMask()) put(PdfName.DECODE, new PdfLiteral("[1 0]"));
        if (image.isInterpolation()) put(PdfName.INTERPOLATE, PdfBoolean.PDFTRUE);
        InputStream is = null;
        try {
            if (image.isImgRaw()) {
                int colorspace = image.colorspace();
                int transparency[] = image.getTransparency();
                if (transparency != null && !image.isMask() && maskRef == null) {
                    String s = "[";
                    for (int k = 0; k < transparency.length; ++k) s += transparency[k] + " ";
                    s += "]";
                    put(PdfName.MASK, new PdfLiteral(s));
                }
                bytes = image.rawData();
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                int bpc = image.bpc();
                if (bpc > 0xff) {
                    if (!image.isMask()) put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    put(PdfName.FILTER, PdfName.CCITTFAXDECODE);
                    int k = bpc - Image.CCITTG3_1D;
                    PdfDictionary decodeparms = new PdfDictionary();
                    if (k != 0) decodeparms.put(PdfName.K, new PdfNumber(k));
                    if ((colorspace & Image.CCITT_BLACKIS1) != 0) decodeparms.put(PdfName.BLACKIS1, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENCODEDBYTEALIGN) != 0) decodeparms.put(PdfName.ENCODEDBYTEALIGN, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENDOFLINE) != 0) decodeparms.put(PdfName.ENDOFLINE, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENDOFBLOCK) != 0) decodeparms.put(PdfName.ENDOFBLOCK, PdfBoolean.PDFFALSE);
                    decodeparms.put(PdfName.COLUMNS, new PdfNumber(image.width()));
                    decodeparms.put(PdfName.ROWS, new PdfNumber(image.height()));
                    put(PdfName.DECODEPARMS, decodeparms);
                } else {
                    switch(colorspace) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            if (image.isInverted()) put(PdfName.DECODE, new PdfLiteral("[1 0]"));
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            if (image.isInverted()) put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0]"));
                            break;
                        case 4:
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted()) put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                    }
                    PdfDictionary additional = image.getAdditional();
                    if (additional != null) putAll(additional);
                    if (image.isMask() && (image.bpc() == 1 || image.bpc() > 8)) remove(PdfName.COLORSPACE);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.bpc()));
                    if (image.isDeflated()) put(PdfName.FILTER, PdfName.FLATEDECODE); else {
                        flateCompress();
                    }
                }
                return;
            }
            String errorID;
            if (image.rawData() == null) {
                is = image.url().openStream();
                errorID = image.url().toString();
            } else {
                is = new java.io.ByteArrayInputStream(image.rawData());
                errorID = "Byte array";
            }
            int i = 0;
            switch(image.type()) {
                case Image.JPEG:
                    put(PdfName.FILTER, PdfName.DCTDECODE);
                    switch(image.colorspace()) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            break;
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                            }
                    }
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    if (image.rawData() != null) {
                        bytes = image.rawData();
                        put(PdfName.LENGTH, new PdfNumber(bytes.length));
                        return;
                    }
                    streamBytes = new ByteArrayOutputStream();
                    transferBytes(is, streamBytes, -1);
                    break;
                default:
                    throw new BadPdfFormatException(errorID + " is an unknown Image format.");
            }
            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
        } catch (IOException ioe) {
            throw new BadPdfFormatException(ioe.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ee) {
                }
            }
        }
    }
}
