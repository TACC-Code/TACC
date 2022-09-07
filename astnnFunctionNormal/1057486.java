class BackupThread extends Thread {
    public synchronized void loadFile(URL midiFile) throws InvalidMidiDataException, MidiUnavailableException {
        stop();
        try {
            originalSequence = MidiSystem.getSequence(midiFile);
            SequenceHandler handler = new SequenceHandler();
            handler.parse(originalSequence);
            currentSequence = handler.copy(originalSequence);
            Map<Integer, SequenceHandler.ChannelMetaData> channels = handler.getChannelData();
            barData = handler.getFrameData();
            countIn = new Sequence(Sequence.PPQ, originalSequence.getResolution());
            tempoData = handler.getTempoData();
            originalSpeed = tempoData.getBPM(0);
            delegate.setTempoInBPM(originalSpeed);
            notifyObservers(originalSpeed);
            data = new Vector<Data>();
            for (SequenceHandler.ChannelMetaData channel : channels.values()) {
                String name = "";
                for (Iterator<Integer> j = channel.programs.iterator(); j.hasNext(); ) {
                    int instrumentNumber = j.next().intValue();
                    if (name.length() > 0) {
                        name += " | ";
                    }
                    if (synthesizer.getDefaultSoundbank() != null) {
                        name += synthesizer.getDefaultSoundbank().getInstrument(new Patch(0, instrumentNumber)).getName();
                    } else {
                        name += MidiEventInterpreter.DEFAULT_INSTRUMENT_NAMES[instrumentNumber];
                    }
                }
                if (name.length() == 0) {
                    name = Messages.getString("MySequencer.Metronome");
                }
                data.add(new Data(name, channel.channel));
            }
            Collections.sort(data);
        } catch (InvalidMidiDataException imde) {
            System.out.println("Unsupported audio file.");
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            currentSequence = null;
            return;
        }
        if (!delegate.isOpen()) {
            delegate.open();
        }
        delegate.setSequence(currentSequence);
        duration = getDuration();
        return;
    }
}
