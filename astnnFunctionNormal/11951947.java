class BackupThread extends Thread {
    public Head(FontFile2 currentFontFile) {
        LogWriter.writeMethod("{readHeadTable}", 0);
        int startPointer = currentFontFile.selectTable(FontFile2.HEAD);
        if (startPointer == 0) LogWriter.writeLog("No head table found"); else {
            int id = currentFontFile.getNextUint32();
            for (int i = 0; i < 3; i++) id = currentFontFile.getNextUint32();
            flags = currentFontFile.getNextUint16();
            unitsPerEm = currentFontFile.getNextUint16();
            for (int i = 0; i < 2; i++) id = currentFontFile.getNextUint64();
            for (int i = 0; i < 4; i++) matrix[i] = currentFontFile.getNextSignedInt16();
            for (int i = 0; i < 3; i++) id = currentFontFile.getNextUint16();
            format = currentFontFile.getNextUint16();
        }
    }
}
