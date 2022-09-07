class BackupThread extends Thread {
    public void render(MidiPacket packet, float[] buffer, int start, int end) {
        int writepos = 0;
        int len = end - start;
        if (packet != null) {
            MidiEvent[] events = packet.events;
            if (events != null) {
                for (int i = 0; i < events.length; i++) {
                    MidiEvent event = events[i];
                    int samplepos = ((int) (event.getTick() * ms_factor)) * channels;
                    if (samplepos != writepos && samplepos <= len) {
                        while (writepos != samplepos) {
                            if ((samplepos - writepos) > 500) {
                                render.read(buffer, writepos + start, writepos + 500 + start);
                                writepos += 500;
                            } else {
                                render.read(buffer, writepos + start, samplepos + start);
                                writepos = samplepos;
                            }
                        }
                    }
                    render.send(event.getMessage());
                }
            }
        }
        if (len != 0) {
            int samplepos = len;
            while (writepos != samplepos) {
                if ((samplepos - writepos) > 500) {
                    render.read(buffer, writepos + start, writepos + 500 + start);
                    writepos += 500;
                } else {
                    render.read(buffer, writepos + start, samplepos + start);
                    writepos = samplepos;
                }
            }
        }
    }
}
