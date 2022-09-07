class BackupThread extends Thread {
    public PropertyDescr(int da, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel) {
        physAddr = da;
        objectIdx = objIdx;
        propertyID = propID;
        propertyIdx = propIdx;
        this.type = type;
        this.maxNoElems = maxNoElems;
        this.readLevel = readLevel;
        this.writeLevel = writeLevel;
    }
}
