class BackupThread extends Thread {
    public Post(FontFile2 currentFontFile) {
        LogWriter.writeMethod("{readPostTable}", 0);
        int startPointer = currentFontFile.selectTable(FontFile2.POST);
        if (startPointer == 0) LogWriter.writeLog("No Post table found"); else {
            int id = (int) (10 * currentFontFile.getFixed());
            currentFontFile.getFixed();
            currentFontFile.getFWord();
            currentFontFile.getFWord();
            currentFontFile.getNextUint16();
            currentFontFile.getNextUint16();
            currentFontFile.getNextUint32();
            currentFontFile.getNextUint32();
            currentFontFile.getNextUint32();
            currentFontFile.getNextUint32();
            int numberOfGlyphs;
            if (id != 30) {
                for (int i = 0; i < 258; i++) this.translateToID.put(this.macEncoding[i], new Integer(i));
            }
            switch(id) {
                case 20:
                    numberOfGlyphs = currentFontFile.getNextUint16();
                    int[] glyphNameIndex = new int[numberOfGlyphs];
                    int numberOfNewGlyphs = 0;
                    for (int i = 0; i < numberOfGlyphs; i++) {
                        glyphNameIndex[i] = currentFontFile.getNextUint16();
                        if ((glyphNameIndex[i] > 257) & (glyphNameIndex[i] < 32768)) numberOfNewGlyphs++;
                    }
                    String[] names = new String[numberOfNewGlyphs];
                    for (int i = 0; i < numberOfNewGlyphs; i++) names[i] = currentFontFile.getString();
                    for (int i = 0; i < numberOfGlyphs; i++) {
                        if ((glyphNameIndex[i] > 257) & (glyphNameIndex[i] < 32768)) this.translateToID.put(names[glyphNameIndex[i] - 258], new Integer(i));
                    }
                    break;
                case 25:
                    numberOfGlyphs = currentFontFile.getNextUint16();
                    int[] glyphOffset = new int[numberOfGlyphs];
                    for (int i = 0; i < numberOfGlyphs; i++) {
                        glyphOffset[i] = currentFontFile.getNextint8();
                        translateToID.put(macEncoding[glyphOffset[i] + i], new Integer(glyphOffset[i]));
                    }
                    break;
            }
        }
    }
}
