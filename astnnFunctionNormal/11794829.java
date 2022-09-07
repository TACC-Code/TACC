class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        errorText.setText("");
        if (src == exit) {
            System.exit(0);
        } else if (src == start) {
            start.setEnabled(false);
            stop.setEnabled(true);
            reset.setEnabled(true);
            restart.setEnabled(true);
            GTM.start();
        } else if (src == stop) {
            stop.setEnabled(false);
            start.setEnabled(true);
            reset.setEnabled(true);
            restart.setEnabled(true);
            GTM.stop();
        } else if (src == reset) {
            reset.setEnabled(false);
            start.setEnabled(true);
            stop.setEnabled(false);
            restart.setEnabled(false);
            GTM.resetHard();
        } else if (src == restart) {
            GTM.restartHard();
        } else if (src == autoReset) {
            if (autoReset.isSelected()) GTM.setAutoReset(true); else GTM.setAutoReset(false);
        } else if (src == speedField) {
            int speed;
            try {
                speed = Integer.parseInt(speedField.getText());
                if (speed < GTM.MIN_PPTICK || speed > GTM.MAX_PPTICK) errorText.setText("Error: speed value out of range."); else GTM.setPixelsPerTick(speed);
            } catch (NumberFormatException x) {
                errorText.setText("Error: illegal numeric format for speed.");
            }
        } else if (src == addThread) {
            int count, delay, times, numThreads;
            try {
                count = Integer.parseInt(nullthrCount.getText());
                delay = Integer.parseInt(nullthrDelay.getText());
                times = Integer.parseInt(nullthrTimes.getText());
                numThreads = GTM.getNumOfThreads();
                nullthr[numThreads] = new NullThread(count, delay, times, new Integer(++nullCount).toString());
                nullthr[numThreads].setInitSleep(nullthrSleep.isSelected());
                nullthr[numThreads].setMessages(true);
                nullthr[numThreads].setPriority(Thread.MIN_PRIORITY);
                GTM.addThread(nullthr[numThreads]);
                nullthr[numThreads].start();
                if (++numThreads == GTM.MAX_THREADS) addThread.setEnabled(false);
                removeThread.setEnabled(true);
                removeAllThreads.setEnabled(true);
            } catch (NumberFormatException x) {
                errorText.setText("Error: illegal numeric format in thread values.");
            }
        } else if (src == removeThread) {
            int index, numThreads;
            try {
                index = Integer.parseInt(removeThrIndex.getText());
                numThreads = GTM.getNumOfThreads();
                if (index < 0 || index > numThreads - 1) errorText.setText("Error: thread index out of bounds."); else {
                    GTM.removeThread(index);
                    if (--numThreads == 0) {
                        removeThread.setEnabled(false);
                        removeAllThreads.setEnabled(false);
                    }
                    addThread.setEnabled(true);
                    nullthr[index].kill();
                    int i;
                    for (i = index; i < numThreads; ++i) nullthr[i] = nullthr[i + 1];
                    nullthr[i] = null;
                }
            } catch (NumberFormatException x) {
                errorText.setText("Error: illegal numeric format in thread index.");
            }
        } else if (src == removeAllThreads) {
            int numThreads = GTM.getNumOfThreads();
            GTM.removeAllThreads();
            addThread.setEnabled(true);
            removeThread.setEnabled(false);
            removeAllThreads.setEnabled(false);
            for (int i = 0; i < numThreads; ++i) {
                nullthr[i].kill();
                nullthr[i] = null;
            }
        } else if (src == addObject) {
            int key = 0;
            int numThreads = GTM.getNumOfObjects();
            if (randInt.isSelected()) key += RandThread.R_I;
            if (randLong.isSelected()) key += RandThread.R_L;
            if (randFloat.isSelected()) key += RandThread.R_F;
            if (randDouble.isSelected()) key += RandThread.R_D;
            randthr[numThreads] = new RandThread(key, new Integer(++randCount).toString());
            randthr[numThreads].setPriority(Thread.MIN_PRIORITY);
            randthr[numThreads].start();
            GTM.addObject(randthr[numThreads]);
            if (++numThreads == GTM.MAX_OBJECTS) addObject.setEnabled(false);
            removeObject.setEnabled(true);
            removeAllObjects.setEnabled(true);
        } else if (src == removeObject) {
            int index, numThreads;
            try {
                index = Integer.parseInt(removeObjIndex.getText());
                numThreads = GTM.getNumOfObjects();
                if (index < 0 || index > GTM.getNumOfObjects() - 1) errorText.setText("Error: object index out of bounds."); else {
                    GTM.removeObject(index);
                    if (--numThreads == 0) {
                        removeObject.setEnabled(false);
                        removeAllObjects.setEnabled(false);
                    }
                    addObject.setEnabled(true);
                    randthr[index].kill();
                    int i;
                    for (i = index; i < numThreads; ++i) randthr[i] = randthr[i + 1];
                    randthr[i] = null;
                }
            } catch (NumberFormatException x) {
                errorText.setText("Error: illegal numeric format in object index.");
            }
        } else if (src == removeAllObjects) {
            int numThreads = GTM.getNumOfObjects();
            GTM.removeAllObjects();
            addObject.setEnabled(true);
            removeObject.setEnabled(false);
            removeAllObjects.setEnabled(false);
            for (int i = 0; i < numThreads; ++i) {
                randthr[i].kill();
                randthr[i] = null;
            }
        }
    }
}
