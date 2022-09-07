class BackupThread extends Thread {
    private void writeRGLine(final SAMReadGroupRecord readGroup) {
        final String[] fields = new String[2 + readGroup.getAttributes().size()];
        fields[0] = HEADER_LINE_START + HeaderRecordType.RG;
        fields[1] = SAMReadGroupRecord.READ_GROUP_ID_TAG + TAG_KEY_VALUE_SEPARATOR + readGroup.getReadGroupId();
        encodeTags(readGroup, fields, 2);
        println(StringUtil.join(FIELD_SEPARATOR, fields));
    }
}
