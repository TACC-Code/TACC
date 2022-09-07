class BackupThread extends Thread {
    @BeforeMethod
    public void initializeModel() {
        context = new ShowContext();
        Show show = ShowBuilder.build(NUMBER_OF_CHANNELS, NUMBER_OF_SUBMASTERS, 2, "");
        context.setShow(show);
        model = new SheetTableModelDetails(context);
        model.addTableModelListener(this);
        Cue cue1 = new Cue("1", "", "", "L 1");
        Cue cue2 = new Cue("2", "", "", "L 1");
        CueDetailFactory f = new CueDetailFactory(NUMBER_OF_CHANNELS, NUMBER_OF_SUBMASTERS);
        f.update(cue1);
        f.update(cue2);
        context.getShow().getCues().add(cue1);
        context.getShow().getCues().add(cue2);
        lightCueDetail1 = (LightCueDetail) cue1.getDetail();
        lightCueDetail2 = (LightCueDetail) cue2.getDetail();
        Channel channel2 = context.getShow().getChannels().get(1);
        Channel channel4 = context.getShow().getChannels().get(3);
        group = new Group();
        group.add(channel2);
        group.add(channel4);
        group.setEnabled(false);
        Groups groups = context.getShow().getGroups();
        groups.add(group);
    }
}
