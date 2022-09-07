class BackupThread extends Thread {
    static void connectInputDevice(final Device device) {
        device.inputSetConnected = true;
        try {
            (device.device == null ? MidiSystem.getTransmitter() : device.device.getTransmitter()).setReceiver(new Receiver() {

                @SuppressWarnings("null")
                public void send(MidiMessage message, long timeStamp) {
                    SEQUENCER.messageReceived(message, device.id);
                    ShortMessage shortMessage = null;
                    boolean isShortMessage = false;
                    int channel = 0;
                    if (message instanceof ShortMessage) {
                        shortMessage = (ShortMessage) message;
                        isShortMessage = true;
                        channel = shortMessage.getChannel();
                        device.channels[channel] = true;
                        if (shortMessage.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                            device.programs[channel] = shortMessage.getData1();
                        }
                    }
                    for (final Device outputDevice : MidiDevicer.DEVICES) {
                        if (outputDevice.isOutputConnected()) {
                            if (outputDevice.isSongConnected() && isShortMessage) {
                                ShortMessage newMessage = new ShortMessage();
                                final int newChannel = CHANNELS[channel];
                                if (device.programs[channel] != PROGRAMS[newChannel]) {
                                    try {
                                        newMessage.setMessage(ShortMessage.PROGRAM_CHANGE, newChannel, device.programs[channel], 0);
                                        PROGRAMS[newChannel] = device.programs[channel];
                                        outputDevice.receiver.send(newMessage, timeStamp);
                                    } catch (InvalidMidiDataException e) {
                                        if (Util.getDebugLevel() > 90) e.printStackTrace();
                                    }
                                    newMessage = new ShortMessage();
                                }
                                try {
                                    newMessage.setMessage(shortMessage.getCommand(), newChannel, shortMessage.getData1(), shortMessage.getData2());
                                } catch (InvalidMidiDataException e) {
                                    newMessage = shortMessage;
                                }
                                try {
                                    outputDevice.receiver.send(newMessage, timeStamp);
                                } catch (IllegalStateException e) {
                                    if (device.open() && outputDevice.open()) {
                                        try {
                                            outputDevice.receiver.send(newMessage, timeStamp);
                                        } catch (IllegalStateException e2) {
                                            KeyboardHero.addStatus(MIDI_UNAVALIABLE);
                                        }
                                    }
                                }
                            } else {
                                try {
                                    outputDevice.receiver.send(message, timeStamp);
                                } catch (IllegalStateException e) {
                                    if (outputDevice.open()) {
                                        try {
                                            outputDevice.receiver.send(message, timeStamp);
                                        } catch (IllegalStateException e2) {
                                            KeyboardHero.addStatus(MIDI_UNAVALIABLE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                public void close() {
                }
            });
            for (int i = 0; i < device.channels.length; ++i) {
                device.channels[i] = false;
            }
            if (device.device != null) device.device.open();
            device.inputConnected = true;
            checkForInputDevice();
        } catch (MidiUnavailableException e) {
            Util.conditionalError(MIDI_UNAVALIABLE, "Err_MidiUnavailable", '(' + device.getName() + ") " + e.getLocalizedMessage());
        } catch (NullPointerException e) {
            Util.conditionalError(DEFAULT_DEVICE_UNAVAILABLE, "Err_DefaultDeviceUnavailable", '(' + device.getName() + ')');
        }
    }
}
