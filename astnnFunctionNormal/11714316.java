class BackupThread extends Thread {
    private void getNextChangingElement(int a0, boolean isWhite, int[] ret) {
        int[] pce = this.prevChangingElems;
        int ces = this.changingElemSize;
        int start = lastChangingElement > 0 ? lastChangingElement - 1 : 0;
        if (isWhite) {
            start &= ~0x1;
        } else {
            start |= 0x1;
        }
        int i = start;
        for (; i < ces; i += 2) {
            int temp = pce[i];
            if (temp > a0) {
                lastChangingElement = i;
                ret[0] = temp;
                break;
            }
        }
        if (i + 1 < ces) {
            ret[1] = pce[i + 1];
        }
    }
}
