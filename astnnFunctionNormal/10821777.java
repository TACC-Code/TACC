class BackupThread extends Thread {
    public void loadState(DataInput input) throws IOException {
        reset();
        isSupervisor = input.readBoolean();
        globalPagesEnabled = input.readBoolean();
        pagingDisabled = input.readBoolean();
        pageCacheEnabled = input.readBoolean();
        writeProtectUserPages = input.readBoolean();
        pageSizeExtensions = input.readBoolean();
        baseAddress = input.readInt();
        lastAddress = input.readInt();
        int len = input.readInt();
        pageSize = new byte[len];
        input.readFully(pageSize, 0, len);
        nonGlobalPages.clear();
        int count = input.readInt();
        for (int i = 0; i < count; i++) nonGlobalPages.add(Integer.valueOf(input.readInt()));
        setSupervisor(isSupervisor);
    }
}
