class BackupThread extends Thread {
    @Override
    protected void read() {
        base = new pspUtilityDialogCommon();
        read(base);
        setMaxSize(base.totalSizeof());
        mode = read32();
        bind = read32();
        overwrite = read32() == 0 ? false : true;
        gameName = readStringNZ(13);
        readUnknown(3);
        saveName = readStringNZ(20);
        saveNameListAddr = read32();
        if (Memory.isAddressGood(saveNameListAddr)) {
            List<String> newSaveNameList = new ArrayList<String>();
            boolean endOfList = false;
            for (int i = 0; !endOfList; i += 20) {
                String saveNameItem = Utilities.readStringNZ(mem, saveNameListAddr + i, 20);
                if (saveNameItem == null || saveNameItem.length() == 0) {
                    endOfList = true;
                } else {
                    newSaveNameList.add(saveNameItem);
                }
            }
            saveNameList = newSaveNameList.toArray(new String[newSaveNameList.size()]);
        }
        fileName = readStringNZ(13);
        readUnknown(3);
        dataBuf = read32();
        dataBufSize = read32();
        dataSize = read32();
        sfoParam = new PspUtilitySavedataSFOParam();
        read(sfoParam);
        icon0FileData = new PspUtilitySavedataFileData();
        read(icon0FileData);
        icon1FileData = new PspUtilitySavedataFileData();
        read(icon1FileData);
        pic1FileData = new PspUtilitySavedataFileData();
        read(pic1FileData);
        snd0FileData = new PspUtilitySavedataFileData();
        read(snd0FileData);
        newDataAddr = read32();
        if (newDataAddr != 0) {
            newData = new PspUtilitySavedataListSaveNewData();
            newData.read(mem, newDataAddr);
        } else {
            newData = null;
        }
        focus = read32();
        abortStatus = read32();
        msFreeAddr = read32();
        msDataAddr = read32();
        utilityDataAddr = read32();
        read8Array(key);
        secureVersion = read32();
        multiStatus = read32();
        idListAddr = read32();
        fileListAddr = read32();
        sizeAddr = read32();
    }
}
