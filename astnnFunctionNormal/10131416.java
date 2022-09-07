class BackupThread extends Thread {
    public double evaluateSong(Song song) {
        double score = 8.0d;
        Track.AbsoluteNote[] notes = new Track.AbsoluteNote[8];
        for (int a = 0; a < song.getNumberOfTracks(); a++) {
            if (song.getTrack(a).getNumberOfNotes() < 8) return song.getTrack(a).getNumberOfNotes();
            if (song.getInstrument(a) < 100) {
                Iterator iterator = song.getTrack(a).getAbsoluteIterator();
                while (iterator.hasNext()) {
                    notes[7] = (Track.AbsoluteNote) iterator.next();
                    if (notes[0] != null) {
                        double[] input = new double[8];
                        for (int b = 0; b < 7; b++) input[b] = (notes[b].getHeight() - notes[b + 1].getHeight()) / 16.0d;
                        score += neuralNetwork.simulate(input)[0];
                    }
                    for (int b = 0; b < 7; b++) notes[b] = notes[b + 1];
                }
            }
        }
        return score;
    }
}
