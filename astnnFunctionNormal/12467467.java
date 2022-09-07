class BackupThread extends Thread {
    public void serialize(final SerializerOutputStream out) throws SerializationException {
        final FieldInfo[] fieldInfo = this.fieldInfo;
        final IndexInfo[] indexInfo = this.indexInfo;
        out.writeByte((byte) 1);
        out.writeString(name.toLowerCase());
        out.writeByte(readWriteType);
        if (fieldInfo != null) {
            final int fieldCount = fieldInfo.length;
            out.writeInt(fieldCount);
            for (int i = 0; i < fieldCount; i++) {
                fieldInfo[i].serialize(out);
            }
        } else {
            out.writeInt(0);
        }
        if (indexInfo != null) {
            final int indexCount = indexInfo.length;
            out.writeInt(indexCount);
            for (int i = 0; i < indexCount; i++) {
                indexInfo[i].serialize(out);
            }
        } else {
            out.writeInt(0);
        }
    }
}
