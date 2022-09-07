class BackupThread extends Thread {
    private void initCAM() {
        CIEXYZ white = (CIEXYZ) this.white.clone();
        double La = white.Y * 0.2;
        white.normalizeY100();
        vc = new ViewingConditions(white, La, 20, Surround.Dim, "Dim");
        cam = new CIECAM02(vc);
        camJNDIndex = new CIECAM02JNDIndex(this.cam, this.white);
        camJNDIArray = new CIECAM02JNDIndex[4];
        for (RGBBase.Channel ch : RGBBase.Channel.RGBWChannel) {
            if (ignoreChannel(ch)) {
                continue;
            }
            this.setupImage(startCode, endCode, ch, 256, endCode - startCode + 1, 1);
            double[][][] XYZValuesImage = getXYZImage();
            double[][] row = XYZValuesImage[0];
            CIECAM02JNDIndex camjndi = new CIECAM02JNDIndex(this.cam, this.white);
            camjndi.setupMonochromeLUT(CIEXYZ.getCIEXYZArray(row));
            camJNDIArray[this.getChannelIndex(ch)] = camjndi;
        }
    }
}
