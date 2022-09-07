class BackupThread extends Thread {
    protected Member[] publishEntryInfo(Object key, Object value) throws ChannelException {
        if (!(key instanceof Serializable && value instanceof Serializable)) return new Member[0];
        Member[] backup = getMapMembers();
        if (backup == null || backup.length == 0) return null;
        MapMessage msg = new MapMessage(getMapContextName(), MapMessage.MSG_COPY, false, (Serializable) key, (Serializable) value, null, channel.getLocalMember(false), backup);
        getChannel().send(getMapMembers(), msg, getChannelSendOptions());
        return backup;
    }
}
