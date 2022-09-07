class BackupThread extends Thread {
    public String marshallTrack(Track track) {
        String s = "<Track";
        s += " title='" + track.getTitle() + "'";
        s += " channel='" + track.getChannel() + "'";
        s += " instrument='" + track.getInstrument() + "'";
        s += ">\n";
        for (int i = 0; i < track.getClips().size(); i++) {
            s += indent(marshallClip(track.getClips().get(i)));
        }
        s += "</Track>\n";
        return s;
    }
}
