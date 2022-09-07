class BackupThread extends Thread {
    public void make() {
        if (astate == 1) return;
        astate = 1;
        JbgsSource s = src;
        if ("".equals(s.getName())) return;
        try {
            InputStream sin = s.getInputStream();
            if (sin == null) {
                System.out.println("Background not found");
                if (noReaderMsg) Jarnal.getJrnlTimerListener().setMessage("Background file not found\n or could not be opened: " + s.getName(), "Problem Loading Background");
                noReaderMsg = true;
                return;
            }
            ZipInputStream zip = new ZipInputStream(sin);
            BufferedImage bg;
            int iind = 0;
            ImageIO.scanForPlugins();
            boolean oof = false;
            for (ZipEntry ze = zip.getNextEntry(); ze != null; ze = zip.getNextEntry()) {
                if (ze.getName().endsWith(".xml")) {
                    oof = true;
                    iind = 0;
                }
                if (!oof) {
                    if (Jarnal.isApplet || Jarnal.showGUI) {
                        ByteArrayOutputStream bbb = new ByteArrayOutputStream();
                        int nmax = 10000;
                        byte bb[] = new byte[nmax + 1];
                        int nread = 0;
                        while ((nread = zip.read(bb, 0, nmax)) >= 0) bbb.write(bb, 0, nread);
                        InputStream ccc = new ByteArrayInputStream(bbb.toByteArray());
                        bg = ImageIO.read(ccc);
                    } else bg = ImageIO.read(zip);
                    if (bg != null) {
                        bgs.add(bg);
                        iind++;
                    }
                }
            }
            if (iind == 0) {
                ImageInputStream iis = ImageIO.createImageInputStream(s.getInputStream());
                Iterator readers = ImageIO.getImageReaders(iis);
                if (readers.hasNext()) {
                    ImageReader reader = (ImageReader) readers.next();
                    reader.setInput(iis, false);
                    ImageReadParam ip = reader.getDefaultReadParam();
                    int np = reader.getNumImages(true);
                    for (int i = 0; i < np; i++) {
                        bg = reader.read(i, ip);
                        if (bg != null) {
                            bgs.add(bg);
                            iind++;
                        }
                    }
                } else {
                    System.out.println("no background reader found");
                    if (noReaderMsg) Jarnal.getJrnlTimerListener().setMessage("No background reader found for: " + s.getName(), "Problem Loading Background");
                    noReaderMsg = false;
                    bgLoad = false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        clearCache();
        initRep();
    }
}
