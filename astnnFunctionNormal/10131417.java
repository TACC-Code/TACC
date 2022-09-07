class BackupThread extends Thread {
    public void learnNetwork(Song song) {
        Track.AbsoluteNote[] notes = new Track.AbsoluteNote[8];
        for (int a = 0; a < song.getNumberOfTracks(); a++) {
            if (song.getInstrument(a) < 100) {
                Iterator iterator = song.getTrack(a).getAbsoluteIterator();
                while (iterator.hasNext()) {
                    notes[7] = (Track.AbsoluteNote) iterator.next();
                    double[] input = new double[8];
                    if (notes[0] != null) {
                        for (int b = 0; b < 7; b++) input[b] = (notes[b].getHeight() - notes[b + 1].getHeight()) / 16.0d;
                        neuralNetwork.learn(input, new double[] { 1.0d });
                    }
                    for (int c = 0; c < 3; c++) {
                        for (int b = 0; b < 7; b++) input[b] = Math.random() * 2.0d - 1.0d;
                        neuralNetwork.learn(input, new double[] { -1.0d });
                    }
                    for (int b = 0; b < 7; b++) notes[b] = notes[b + 1];
                }
            }
        }
    }
}
