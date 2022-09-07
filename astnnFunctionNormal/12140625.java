class BackupThread extends Thread {
    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        Status = in.readInt();
        Area = in.readInt();
        Name = in.readString(16);
        Description = in.readString(76);
        Password = in.readString(32);
        in.skip(4);
        Size = in.readInt();
        FileTime = in.readLong();
        LastAccessed = in.readLong();
        NeverOverwrite = in.readBoolean();
        NeverDelete = in.readBoolean();
        FreeFile = in.readBoolean();
        CopyBeforeDownload = in.readBoolean();
        Offline = in.readBoolean();
        FailedScan = in.readBoolean();
        FreeTime = in.readBoolean();
        Downloads = in.readInt();
        Cost = in.readInt();
        Uploader = new TUserInfo();
        Uploader.readFrom(in);
        UserInfo = in.readInt();
        HasLongDescription = in.readBoolean();
        in.skip(44 * 1);
    }
}
