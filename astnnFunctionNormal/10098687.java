class BackupThread extends Thread {
                        public void actionPerformed(ActionEvent e) {
                            boolean needNL = false;
                            for (javax.sound.midi.MidiChannel c : synthesizer.getChannels()) {
                                if (c != null) {
                                    if (c.getMute()) {
                                        System.out.print(" m");
                                    } else {
                                        System.out.print(" " + ((c.getController(7) * 128) + c.getController(39)));
                                    }
                                    if (c.getSolo()) System.out.print("s");
                                    needNL = true;
                                }
                            }
                            if (needNL) System.out.println();
                            return;
                        }
}
