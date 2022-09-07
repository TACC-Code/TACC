class BackupThread extends Thread {
    public static Glyph getFontGlyph(String path, Character c, float pointSize) throws FontFormatException, IOException, GlyphException {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        FontDelegate fontDelegate = null;
        FileInputStream fis = null;
        FileChannel fc = null;
        try {
            fis = new FileInputStream(path);
            fc = fis.getChannel();
            ByteBuffer buf = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fontDelegate = FontFactory.createFonts(buf)[0];
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    if (fc != null) {
                        fc.close();
                    }
                } catch (IOException ignore) {
                }
            }
        }
        StringCharacterIterator iter = new StringCharacterIterator(c.toString());
        GNUGlyphVector gv = fontDelegate.createGlyphVector(pointSize, new AffineTransform(), frc, iter);
        if (gv == null || gv.getGlyphContours(0).length == 0) {
            throw new GlyphException(String.format("Glyph <%c> is invalid.%n", c));
        }
        return new Glyph(gv.getGlyphContours(0), gv.getLogicalBounds());
    }
}
