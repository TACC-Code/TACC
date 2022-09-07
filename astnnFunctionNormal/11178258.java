class BackupThread extends Thread {
    public void writeToParcel(Parcel parcel) {
        parcel.writeLong(id);
        parcel.writeString(uri.toString());
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeLong(date.getTime());
        parcel.writeString(link.toString());
        parcel.writeString(content);
        parcel.writeString(type);
        parcel.writeByte(read ? (byte) 1 : (byte) 0);
    }
}
