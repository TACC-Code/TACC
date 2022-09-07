class BackupThread extends Thread {
    @Override
    public void run() {
        final int[] opponentCards = new int[MonteCarlo.NB_HOLECARDS];
        for (int i = 0; i < m_nbSimulations; ++i) {
            for (int j = 0; j < m_nbMissingTableCards; ++j) {
                m_dynamicTable[m_nbCardsShowedOnTable + j] = m_deck[j];
            }
            final long boardCode = (MonteCarlo.Encode(m_dynamicTable, 0));
            final long myResult = HandEvaluator.hand7Eval(MonteCarlo.Encode(m_playerCards, boardCode));
            boolean lost = false;
            for (int j = m_nbMissingTableCards; j < m_nbCardsToDraw; j += 2) {
                opponentCards[0] = m_deck[j];
                opponentCards[1] = m_deck[j + 1];
                final int opponentResult = HandEvaluator.hand7Eval(MonteCarlo.Encode(opponentCards, boardCode));
                if (opponentResult > myResult) {
                    lost = true;
                    break;
                }
            }
            if (!lost) {
                m_gamesWon++;
            }
            for (int k = 0; k != m_nbCardsToDraw; ++k) {
                final int l = m_myRandom.nextInt(m_deck.length - k) + k;
                final int tmp = m_deck[l];
                m_deck[l] = m_deck[k];
                m_deck[k] = tmp;
            }
        }
    }
}
