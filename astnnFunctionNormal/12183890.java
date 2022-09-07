class BackupThread extends Thread {
    public String toString() {
        String str = new String();
        str = getVersion() + " " + getLayer() + "\nBitRate:\t\t\t" + getBitRate() + "kbps\nSampleRate:\t\t\t" + getSampleRate() + "Hz\nChannelMode:\t\t\t" + getChannelMode() + "\nCopyrighted:\t\t\t" + isCopyrighted() + "\nOriginal:\t\t\t" + isOriginal() + "\nCRC:\t\t\t\t" + isProtected() + "\nEmphasis:\t\t\t" + getEmphasis() + "\nOffset:\t\t\t\t" + getLocation() + "\nPrivateBit:\t\t\t" + privateBitSet() + "\nPadding:\t\t\t" + hasPadding() + "\nFrameLength:\t\t\t" + getFrameLength() + "\nVBR:\t\t\t\t" + isVBR() + "\nNumFrames:\t\t\t\t" + getNumFrames();
        if (isVBR()) {
            str += "\n" + xingHead.toString();
        }
        return str;
    }
}
