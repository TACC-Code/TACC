class BackupThread extends Thread {
    @Override
    public String toXMLString(int base_space) {
        String str = new String();
        str += (MyUTIL.whiteSpaceStr(base_space) + "<TVCT>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<table_id>" + TableID.TERRESTRIAL_VIRTUAL_CHANNEL_TABLE.getValue() + "</table_id>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<transport_stream_id>" + transport_stream_id + "</transport_stream_id>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<version_number>" + version_number + "</version_number>\n");
        if (getNumChannels() > 0) {
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<TVCTChannelLoop>\n");
            Iterator<TVCTChannel> it = getChannels();
            while (it.hasNext()) str += it.next().toXMLString(base_space + 4);
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "</TVCTChannelLoop>\n");
        }
        if (getDescriptorSize() > 0) {
            Iterator<Descriptor> it = getDescriptors();
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<DescriptorLoop>\n");
            while (it.hasNext()) str += it.next().toXMLString(base_space + 4);
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "</DescriptorLoop>\n");
        }
        str += (MyUTIL.whiteSpaceStr(base_space) + "</TVCT>\n");
        return str;
    }
}
