class BackupThread extends Thread {
    @Override
    public Section[] toSection() {
        Section[] sections;
        int max_stream_size_in_section = 1021 - (13 + getDescriptorsLength());
        int total_section = getTotalSectionNumber(), write_from_idx = 0;
        sections = new Section[total_section];
        for (int sn = 0; sn < sections.length; sn++) {
            sections[sn] = SectionFactory.createTVCTSection(this, transport_stream_id, sn, total_section - 1);
            int write_to_idx = write_from_idx;
            int stream_size = 0;
            while (write_to_idx < getNumChannels() && (stream_size + getChannelAt(write_to_idx).getSizeInBytes()) < max_stream_size_in_section) stream_size += getChannelAt(write_to_idx++).getSizeInBytes();
            int total_bits = (2 + stream_size + 2 + getDescriptorsLength()) * Byte.SIZE;
            BitOutputStream os = new BitOutputStream(total_bits);
            os.writeFromLSB(0, 8);
            os.writeFromLSB(getNumChannels(), 8);
            for (int n = write_from_idx; n < write_to_idx; n++) os.write(getChannelAt(n).toByteArray());
            os.writeFromLSB(0xFF, 6);
            os.writeFromLSB(getDescriptorsLength(), 10);
            if (getDescriptorSize() > 0) {
                Iterator<Descriptor> it = getDescriptors();
                while (it.hasNext()) os.write(it.next().toByteArray());
            }
            sections[sn].setPrivateData(os.toByteArray());
            write_from_idx = write_to_idx;
        }
        return sections;
    }
}
