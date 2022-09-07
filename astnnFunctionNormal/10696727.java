class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    void loadFromDB() {
        assert (projectFrame == null);
        ProjectContainer project = null;
        try {
            project = new ProjectContainer();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            project = new ProjectContainer();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (songEJB.getVoiceResources() != null) {
            for (VoiceResourceEJB resource : songEJB.getVoiceResources()) {
                resource = context.loadVoiceResource(resource.getId());
                if (resource instanceof VoiceResourceEJB) {
                    MidiDeviceDescriptor dev = (MidiDeviceDescriptor) resource.getObject();
                    System.out.println("MidiDeviceDescriptor: serializable  midiDev " + dev.getSerializableMidiDevice() + "  " + dev.getMidiDevice());
                    project.getMidiDeviceDescriptors().add(dev);
                }
            }
        }
        project.installMidiDevices();
        for (LaneEJB laneEJB : songEJB.getLanes()) {
            MidiLane lane = project.createMidiLane();
            MidiDeviceVoiceInfo voiceInfo = (MidiDeviceVoiceInfo) laneEJB.getVoiceInfo();
            if (voiceInfo != null) {
                for (Long id : voiceInfo.getResourceList()) {
                    VoiceResourceEJB resourceEJB = context.loadVoiceResource(id);
                    MidiDeviceDescriptor dev = (MidiDeviceDescriptor) resourceEJB.getObject();
                    if (dev != null) {
                        lane.getTrack().setMidiDevice(dev.getMidiDevice());
                        lane.setMidiChannel(voiceInfo.getChannel());
                        lane.setProgram(voiceInfo.getMyPatch());
                    }
                }
            }
            for (PartEJB partEJB : laneEJB.getParts()) {
                MidiPart part = new MidiPart(lane);
                long t1 = partEJB.getTime();
                long dur = partEJB.getDuration();
                part.setStartTick(t1);
                part.setEndTick(t1 + dur);
                long dataID = partEJB.getResource().getId();
                PartResourceEJB dataEJB = context.loadPartResource(dataID);
                Object o = dataEJB.getObject();
                if (o instanceof ArrayList) {
                    ArrayList<MultiEvent> list = (ArrayList<MultiEvent>) o;
                    for (MultiEvent ev : list) {
                        MultiEvent clone;
                        try {
                            clone = (MultiEvent) ev.clone();
                            clone.deepMove(t1);
                            part.add(clone);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println(" ooops did not expect to see a: " + o);
                }
            }
        }
        project.getProjectLane().onLoad();
        project.rebuildGUI();
        try {
            projectFrame = new ProjectFrame(project);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
