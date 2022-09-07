class BackupThread extends Thread {
    public static void main(String args[]) {
        GUI.setBlueLookAndFeel();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                MixerDialog m = new MixerDialog(null, true);
                Mixer mixer = new Mixer();
                for (int i = 0; i < 3; i++) {
                    mixer.getChannels().addChannel(new Channel());
                }
                m.setMixer(mixer);
                m.setVisible(true);
                m.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }
        });
    }
}
