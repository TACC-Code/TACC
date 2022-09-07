class BackupThread extends Thread {
    public boolean read(WriteBufferExt writeExt) {
        if (m_fileManagerEntry.isDummyFile()) return false;
        if (isVariableLength()) {
            long lLastHeaderStartPosition = m_fileManagerEntry.m_dataFile.getFileCurrentPosition();
            LineRead header = m_fileManagerEntry.m_dataFile.readBuffer(4, false);
            if (header != null) {
                int nLengthExcludingHeader = header.getAsLittleEndingUnsignBinaryInt();
                LineRead lineRead = m_fileManagerEntry.m_dataFile.readBuffer(nLengthExcludingHeader, true);
                m_fileManagerEntry.m_dataFile.setLastPosition(lLastHeaderStartPosition);
                if (lineRead != null) {
                    writeExt.setFromLineRead(lineRead, 0);
                    int n = getVariableRecordLength(nLengthExcludingHeader);
                    writeExt.setVariableRecordWholeLength(n);
                    return true;
                }
            }
            return false;
        } else {
            if (m_fileManagerEntry.m_dataFile.isEOF()) return false;
            int nRecordLength = getRecordLength(null);
            LineRead lineRead = null;
            if (nRecordLength > 0) lineRead = m_fileManagerEntry.m_dataFile.readBuffer(nRecordLength, true); else lineRead = m_fileManagerEntry.m_dataFile.readNextUnixLine();
            if (lineRead != null) {
                writeExt.setFromLineRead(lineRead, 0);
                incNbRecordRead();
                return true;
            }
            return false;
        }
    }
}
