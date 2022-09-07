class BackupThread extends Thread {
    private void parseShortMessage(ShortMessage message, long timestamp) {
        int track = message.getChannel();
        switch(message.getCommand()) {
            case ShortMessage.PROGRAM_CHANGE:
                Logger.getRootLogger().trace("Program change to " + message.getData1());
                Instrument instrument = new Instrument((byte) message.getData1());
                fireTimeEvent(new Time(timestamp));
                fireVoiceEvent(new Voice((byte) track));
                fireInstrumentEvent(instrument);
                break;
            case ShortMessage.CONTROL_CHANGE:
                Logger.getRootLogger().trace("Controller change to " + message.getData1() + ", value = " + message.getData2());
                Controller controller = new Controller((byte) message.getData1(), (byte) message.getData2());
                fireTimeEvent(new Time(timestamp));
                fireVoiceEvent(new Voice((byte) track));
                fireControllerEvent(controller);
                break;
            case ShortMessage.NOTE_ON:
                if (message.getData2() == 0) {
                    noteOffEvent(timestamp, track, message.getData1(), message.getData2());
                } else {
                    noteOnEvent(timestamp, track, message.getData1(), message.getData2());
                }
                break;
            case ShortMessage.NOTE_OFF:
                noteOffEvent(timestamp, track, message.getData1(), message.getData2());
                break;
            case ShortMessage.CHANNEL_PRESSURE:
                Logger.getRootLogger().trace("Channel pressure, pressure = " + message.getData1());
                ChannelPressure pressure = new ChannelPressure((byte) message.getData1());
                fireTimeEvent(new Time(timestamp));
                fireVoiceEvent(new Voice((byte) track));
                fireChannelPressureEvent(pressure);
                break;
            case ShortMessage.POLY_PRESSURE:
                Logger.getRootLogger().trace("Poly pressure on key " + message.getData1() + ", pressure = " + message.getData2());
                PolyphonicPressure poly = new PolyphonicPressure((byte) message.getData1(), (byte) message.getData2());
                fireTimeEvent(new Time(timestamp));
                fireVoiceEvent(new Voice((byte) track));
                firePolyphonicPressureEvent(poly);
                break;
            case ShortMessage.PITCH_BEND:
                Logger.getRootLogger().trace("Pitch Bend, data1= " + message.getData1() + ", data2= " + message.getData2());
                PitchBend bend = new PitchBend((byte) message.getData1(), (byte) message.getData2());
                fireTimeEvent(new Time(timestamp));
                fireVoiceEvent(new Voice((byte) track));
                firePitchBendEvent(bend);
                break;
            default:
                Logger.getRootLogger().trace("Unparsed message: " + message.getCommand());
                break;
        }
    }
}
