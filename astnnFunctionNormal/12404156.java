class BackupThread extends Thread {
    public Sequence scoreToSeq(Score score) throws InvalidMidiDataException {
        PO.p("score to seq \n" + score);
        Sequence seq = new Sequence(Sequence.PPQ, resolution_ppqn);
        ScoreTracker st = new ScoreTracker();
        st.longestTime = 0;
        Enumeration partList = score.getPartList().elements();
        while (partList.hasMoreElements()) {
            Part part = (Part) partList.nextElement();
            st.currChannel = ((part.getChannel() - 1) % 16) + 1;
            st.currPort = (int) (part.getChannel() * 1.0 / 16.0);
            st.currTrack = seq.createTrack();
            addInstrument(st, part.getInstrument());
            Enumeration phrases = part.getPhraseList().elements();
            while (phrases.hasMoreElements()) {
                Phrase phrase = (Phrase) phrases.nextElement();
                st.phraseTick = (long) (phrase.getStartTime() * resolution_ppqn);
                Enumeration notes = phrase.getNoteList().elements();
                while (notes.hasMoreElements()) {
                    Note note = (Note) notes.nextElement();
                    noteToEvents(note, st);
                }
            }
        }
        addCallBacksToSeq(st.longestTime, seq);
        addEndEvent(st.longestTrack, st.longestTime);
        return seq;
    }
}
