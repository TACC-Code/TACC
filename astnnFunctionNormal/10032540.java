class BackupThread extends Thread {
    public Plucked2() {
        ModelOscillator osc = new ModelOscillator() {

            public float getAttenuation() {
                return 0;
            }

            public int getChannels() {
                return 1;
            }

            public ModelOscillatorStream open(float samplerate) {
                return new MyOscillatorStream(samplerate);
            }
        };
        ModelPerformer performer = new ModelPerformer();
        performer.getOscillators().add(osc);
        if (true) {
            performer.getConnectionBlocks().add(new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "attack", 0))));
            performer.getConnectionBlocks().add(new ModelConnectionBlock(1000.0, new ModelDestination(new ModelIdentifier("eg", "sustain", 0))));
            performer.getConnectionBlocks().add(new ModelConnectionBlock(1200.0, new ModelDestination(new ModelIdentifier("eg", "release", 0))));
        }
        SimpleInstrument ins = new SimpleInstrument();
        ins.setName("Simple Pluck2");
        ins.add(performer);
        ins.setPatch(new Patch(0, 0));
        addInstrument(ins);
    }
}
