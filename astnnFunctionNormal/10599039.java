class BackupThread extends Thread {
    @Before
    public void setUp() throws Exception {
        AudioFormat pcma = FormatFactory.createAudioFormat("pcma", 8000, 8, 1);
        Formats fmts = new Formats();
        fmts.add(pcma);
        Formats dstFormats = new Formats();
        dstFormats.add(FormatFactory.createAudioFormat("LINEAR", 8000, 16, 1));
        dspFactory.addCodec("org.mobicents.media.server.impl.dsp.audio.g711.alaw.Encoder");
        dspFactory.addCodec("org.mobicents.media.server.impl.dsp.audio.g711.alaw.Decoder");
        dsp11 = dspFactory.newProcessor();
        dsp12 = dspFactory.newProcessor();
        dsp21 = dspFactory.newProcessor();
        dsp22 = dspFactory.newProcessor();
        clock = new DefaultClock();
        scheduler = new Scheduler(4);
        scheduler.setClock(clock);
        scheduler.start();
        udpManager = new UdpManager(scheduler);
        udpManager.start();
        rtpManager = new RTPManager(udpManager);
        rtpManager.setBindAddress("127.0.0.1");
        rtpManager.setScheduler(scheduler);
        source1 = new Sine(scheduler);
        source1.setFrequency(50);
        source1.setDsp(dsp11);
        source1.setFormats(fmts);
        source2 = new Sine(scheduler);
        source2.setFrequency(50);
        source2.setDsp(dsp21);
        source2.setFormats(fmts);
        analyzer1 = new SpectraAnalyzer("analyzer", scheduler);
        analyzer1.setDsp(dsp12);
        analyzer1.setFormats(dstFormats);
        analyzer2 = new SpectraAnalyzer("analyzer", scheduler);
        analyzer2.setDsp(dsp22);
        analyzer2.setFormats(dstFormats);
        channel1 = rtpManager.getChannel();
        channel2 = rtpManager.getChannel();
        channel1.bind();
        channel2.bind();
        channel1.setPeer(new InetSocketAddress("127.0.0.1", channel2.getLocalPort()));
        channel2.setPeer(new InetSocketAddress("127.0.0.1", channel1.getLocalPort()));
        channel1.setFormatMap(AVProfile.audio);
        channel2.setFormatMap(AVProfile.audio);
        txPipe1 = new PipeImpl();
        rxPipe1 = new PipeImpl();
        txPipe2 = new PipeImpl();
        rxPipe2 = new PipeImpl();
        txPipe1.connect(source1);
        txPipe1.connect(channel1.getOutput());
        rxPipe1.connect(analyzer1);
        rxPipe1.connect(channel1.getInput());
        txPipe2.connect(source2);
        txPipe2.connect(channel2.getOutput());
        rxPipe2.connect(analyzer2);
        rxPipe2.connect(channel2.getInput());
    }
}
