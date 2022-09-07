class BackupThread extends Thread {
    public String toString() {
        return "Mp3Header[" + offset + "] {" + "\n MPEG Version: " + getMPEGVersion() + "\n Layer description: " + getLayerDescription() + "\n Is CRC protected: " + isCRCProtected() + "\n Bitrate: " + getBitRate() + "\n Samplerate: " + getSampleRate() + "\n Is Padded: " + isPadded() + "\n Channel mode: " + getChannelMode() + "\n Mode Extension: " + getModeExtension() + "\n Copyright: " + isCopyrighted() + "\n Is original: " + isOriginal() + "\n Emphasis: " + getEmphasis() + "\n Frame size: " + getFrameSize() + "\n}";
    }
}
