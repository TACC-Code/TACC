class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        String filename = null;
        boolean automatic = false;
        if (args.length == 0) {
            printUsageAndExit();
        } else if (args.length == 1) {
            filename = args[0];
        } else if (args.length == 2) {
            if (!args[0].equals("-q")) {
                printUsageAndExit();
            } else {
                filename = args[0];
                automatic = true;
            }
        } else {
            printUsageAndExit();
        }
        File midiFile = new File(filename);
        Sequence seq = null;
        try {
            StandardMidiFileReader reader = new StandardMidiFileReader();
            seq = reader.getSequence(midiFile);
        } catch (java.io.IOException e) {
            System.out.println("Error trying to load file '" + filename + "'");
            System.out.println(e.toString());
            System.exit(-1);
        } catch (javax.sound.midi.InvalidMidiDataException e) {
            System.out.println("Error trying to read contents of file '" + filename + "'");
            System.out.println(e.toString());
            System.exit(-1);
        }
        String partName = midiFile.getName();
        double loopStart = 0;
        double loopEnd = MidiUtil.microsecondToBeat(seq.getMicrosecondLength(), MidiUtil.getTempo(seq));
        double loopEndDiff = loopEnd - (((int) loopEnd));
        if (loopEndDiff != 0) {
            loopEnd = (int) loopEnd + 1;
            System.out.println("Rounding loopEnd from " + MidiUtil.microsecondToBeat(seq.getMicrosecondLength(), MidiUtil.getTempo(seq)) + " to " + loopEnd);
        }
        double loopNumberOfTimes = 32.0 / (loopEnd - loopStart);
        boolean isPercussive = false;
        boolean[] channelPresence = doors.util.MidiUtil.getChannelPresence(seq);
        if (channelPresence[9]) isPercussive = true;
        boolean save = true;
        if (!automatic) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println(e.toString());
            }
            Dialog dialog = new Dialog(partName, loopStart, loopEnd, loopNumberOfTimes, isPercussive);
            dialog.setVisible(true);
            partName = dialog.getPartName();
            loopStart = dialog.getLoopStart();
            loopEnd = dialog.getLoopEnd();
            loopNumberOfTimes = dialog.getNumberOfTimes();
            isPercussive = dialog.isPercussive();
            save = dialog.getSave();
        }
        if (save) {
            DocType docType = new DocType("part", "http://doors.sourceforge.net/doors.dtd");
            Element part = new Element("part");
            Document doc = new Document(part, docType);
            XmlUtil.setXPathValue(part, "name", partName);
            XmlUtil.setXPathValue(part, "id", "");
            XmlUtil.setXPathValue(part, "state", "new");
            XmlUtil.setXPathValue(part, "startTime", "");
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/name", midiFile.getName());
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/id", "");
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/state", "new");
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/specification/instances", "");
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/specification/factories", "");
            if (isPercussive) {
                XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/specification/interfaces/spec/name", "IDL:doors/midifileplayer/PercussiveGeneralMidiFilePlayer:1.0");
            } else {
                XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/specification/interfaces/spec/name", "IDL:doors/midifileplayer/MelodicGeneralMidiFilePlayer:1.0");
            }
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/recommendations", "");
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/match", "");
            double defaultTempo = doors.util.Util.toOneDP(MidiUtil.getTempo(seq));
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/defaultTempo", "" + defaultTempo);
            String mediaFilename = midiFile.getName().replaceAll(" ", "%20");
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/mediaUrl", "$(URLPATH)/" + mediaFilename);
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/loopStart", "" + loopStart);
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/loopEnd", "" + loopEnd);
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/numberOfTimes", "" + loopNumberOfTimes);
            XmlUtil.setXPathValue(part, "deviceElements/doors.MediaFilePlayer/offset", "0");
            String outputFileName = "";
            if (midiFile.getParent() != null) {
                outputFileName = midiFile.getParent() + System.getProperty("file.separator") + partName + ".xml";
            } else {
                outputFileName = partName + ".xml";
            }
            doors.util.Util.writeFile(new java.io.File(outputFileName), XmlUtil.documentToString(doc));
        } else {
        }
        System.exit(0);
    }
}
