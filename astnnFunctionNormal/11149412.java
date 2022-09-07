class BackupThread extends Thread {
    private double getCIECAM02Luminance(double JNDIndex, RGBBase.Channel ch) {
        double lightness = camJNDIndex.getLightness(JNDIndex);
        int index = getChannelIndex(ch);
        return camJNDIArray[index].getMonochromeLuminance(lightness);
    }
}
