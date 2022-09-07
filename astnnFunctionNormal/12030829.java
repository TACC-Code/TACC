class BackupThread extends Thread {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setIcon(null);
        AudioFormat format = (AudioFormat) value;
        String text = "" + ((int) format.getSampleRate()) + " Hz, " + format.getSampleSizeInBits() + " bit " + (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED ? "signed, " : "unsigned, ") + (format.getChannels() == 2 ? "stereo" : "mono");
        setText(text);
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
        return this;
    }
}
