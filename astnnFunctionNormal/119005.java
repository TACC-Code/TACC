class BackupThread extends Thread {
    public static void main(String[] args) {
        AudioFormat.Encoding targetEncoding = null;
        String strFileTypeName = null;
        String strFileTypeExtension = null;
        AudioFileFormat.Type fileType = null;
        List<String> vorbisComments = new ArrayList<String>();
        int nQuality = -1;
        int nBitrate = -1;
        Getopt g = new Getopt("AudioEncoder", args, "he:b:q:t:T:V:D");
        int c;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'h':
                    printUsageAndExit();
                case 'e':
                    targetEncoding = new AudioFormat.Encoding(g.getOptarg());
                    if (DEBUG) out("AudioEncoder.main(): using encoding: " + targetEncoding);
                    break;
                case 'q':
                    nQuality = Integer.parseInt(g.getOptarg());
                    break;
                case 'b':
                    nBitrate = Integer.parseInt(g.getOptarg());
                    break;
                case 't':
                    strFileTypeName = g.getOptarg();
                    break;
                case 'T':
                    strFileTypeExtension = g.getOptarg();
                    break;
                case 'V':
                    vorbisComments.add(g.getOptarg());
                    if (DEBUG) out("adding comment: " + g.getOptarg());
                    break;
                case 'D':
                    DEBUG = true;
                    break;
                case '?':
                    printUsageAndExit();
                default:
                    out("getopt() returned " + c);
                    break;
            }
        }
        if (targetEncoding == null) {
            out("AudioEncoder.main(): no encoding specified!");
            printUsageAndExit();
        }
        if (strFileTypeName != null && strFileTypeExtension != null) {
            fileType = new AudioFileFormat.Type(strFileTypeName, strFileTypeExtension);
        } else {
            fileType = AudioFileFormat.Type.WAVE;
        }
        if (DEBUG) out("AudioEncoder.main(): using file type: " + fileType);
        int nOptionIndex = g.getOptind();
        if (args.length - nOptionIndex != 2) {
            printUsageAndExit();
        }
        File pcmFile = new File(args[nOptionIndex]);
        File encodedFile = new File(args[nOptionIndex + 1]);
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(pcmFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ais == null) {
            out("cannot open audio file");
            System.exit(1);
        }
        AudioFormat targetAudioFormat = null;
        if (vorbisComments.size() > 0 || nQuality != -1 || nBitrate != -1) {
            Map<String, Object> properties = new HashMap<String, Object>();
            if (nQuality != -1) {
                properties.put("quality", new Integer(nQuality));
            }
            if (nBitrate != -1) {
                properties.put("bitrate", new Integer(nBitrate));
            }
            if (vorbisComments.size() > 0) {
                properties.put("vorbis.comments", vorbisComments);
                if (DEBUG) out("adding vorbis comments to properties map");
            }
            AudioFormat sourceFormat = ais.getFormat();
            targetAudioFormat = new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), AudioSystem.NOT_SPECIFIED, sourceFormat.getChannels(), AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, sourceFormat.isBigEndian(), properties);
        }
        AudioInputStream encodedAudioInputStream = null;
        if (targetAudioFormat != null) {
            encodedAudioInputStream = AudioSystem.getAudioInputStream(targetAudioFormat, ais);
        } else {
            encodedAudioInputStream = AudioSystem.getAudioInputStream(targetEncoding, ais);
        }
        if (DEBUG) {
            out("encoded stream: " + encodedAudioInputStream);
        }
        if (DEBUG) {
            out("encoded stream's format: " + encodedAudioInputStream.getFormat());
        }
        int nWrittenFrames = 0;
        try {
            nWrittenFrames = AudioSystem.write(encodedAudioInputStream, fileType, encodedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
