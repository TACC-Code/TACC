class BackupThread extends Thread {
        FinishedMatchRegular getStatsMessage() {
            MauMauPlayerWrapper[] sort = new MauMauPlayerWrapper[indexOfNextSubscriberToAdd];
            System.arraycopy(players, 0, sort, 0, sort.length);
            MauMauPlayerWrapper s;
            for (int o = 0; o < sort.length; o++) for (int i = 0; i < sort.length - o - 1; i++) if (sort[i + 1].getMinusPoints() < sort[i].getMinusPoints()) {
                s = sort[i];
                sort[i] = sort[i + 1];
                sort[i + 1] = s;
            }
            if (matchEndsWithGamesWon()) {
                for (int o = 0; o < sort.length; o++) for (int i = 0; i < sort.length - o - 1; i++) if (sort[i].getGamesWon() < sort[i + 1].getGamesWon()) {
                    s = sort[i];
                    sort[i] = sort[i + 1];
                    sort[i + 1] = s;
                }
            }
            String[] playersNames = new String[sort.length];
            int[] playersMali = new int[sort.length];
            int[] playersGames = new int[sort.length];
            for (int i = 0; i < sort.length; i++) {
                playersNames[i] = sort[i].getName();
                playersMali[i] = sort[i].getMinusPoints();
                playersGames[i] = sort[i].getGamesWon();
            }
            return new FinishedMatchRegular(getName(), playersNames, playersMali, playersGames);
        }
}
