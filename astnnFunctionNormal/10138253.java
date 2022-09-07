class BackupThread extends Thread {
    public void get(byte[] buffer, int channel) {
        int framesize_pc = (format.getFrameSize() / format.getChannels());
        int c_len = size * framesize_pc;
        if (converter_buffer == null || converter_buffer.length < c_len) converter_buffer = new byte[c_len];
        if (format.getChannels() == 1) {
            converter.toByteArray(array(), size, buffer);
        } else {
            converter.toByteArray(array(), size, converter_buffer);
            if (channel >= format.getChannels()) return;
            int z_stepover = format.getChannels() * framesize_pc;
            int k_stepover = framesize_pc;
            for (int j = 0; j < framesize_pc; j++) {
                int k = j;
                int z = channel * framesize_pc + j;
                for (int i = 0; i < size; i++) {
                    buffer[z] = converter_buffer[k];
                    z += z_stepover;
                    k += k_stepover;
                }
            }
        }
    }
}
