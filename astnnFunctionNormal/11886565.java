class BackupThread extends Thread {
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(serviceName);
        dest.writeString(name);
        dest.writeString(protocolUid);
        dest.writeString(ownerUid);
        dest.writeByte(serviceId);
        dest.writeByte(status);
        dest.writeByte(xstatus);
        dest.writeString(xstatusName);
        dest.writeString(xstatusDescription);
        dest.writeString(externalIP);
        dest.writeInt(onlineTime);
        dest.writeLong(signonTime != null ? signonTime.getTime() : -1);
        dest.writeByte(visibility);
        dest.writeByte(unread);
        dest.writeByte((byte) (canFileShare ? 1 : 0));
        dest.writeInt(groupId);
        dest.writeString(iconHash);
        dest.writeString(clientId);
    }
}
