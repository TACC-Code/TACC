class BackupThread extends Thread {
    @Override
    public int getTotalSectionNumber() {
        int max_stream_size_in_section = 1021 - (13 + getDescriptorsLength());
        int total_section = 0;
        for (int stream_index = 0; stream_index < getNumChannels(); ) {
            int stream_size = 0;
            while (stream_index < getNumChannels() && (stream_size + getChannelAt(stream_index).getSizeInBytes()) < max_stream_size_in_section) stream_size += getChannelAt(stream_index++).getSizeInBytes();
            total_section++;
        }
        return total_section;
    }
}
