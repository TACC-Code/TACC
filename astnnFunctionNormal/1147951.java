class BackupThread extends Thread {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ChannelFactory caF = ChannelFactory.defaultFactory();
                cav = myWindow.myDocument.getCavSelector().getSelectedRfCavity();
                if (dCavFieldSP < 0.5) {
                    try {
                        Channel ca1 = caF.getChannel("SCL_HPRF:Tun" + cav.channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Mot");
                        tuneRev = tfTune.getValue();
                        ca1.putVal(ca1.getValDbl() + tuneRev);
                        Thread.sleep(2500);
                        Channel ca2 = caF.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
                        pulseWidthdt = ca2.getValDbl();
                        myWindow.myDocument.getCavSelector().getCavPhaseAvg(1298., 1308., 0);
                    } catch (ConnectionException ce) {
                        System.out.println("Cannot connect to PV.");
                    } catch (GetException ge) {
                        System.out.println("Cannot get PV value.");
                    } catch (PutException pe) {
                        System.out.println("Cannot write to PV.");
                    } catch (InterruptedException ie) {
                        System.out.println("Cannot pause for tunning!");
                    }
                } else {
                    System.out.println("You may not detune this cavity, it's on!");
                }
            }
}
