class BackupThread extends Thread {
    private static int getChannelCount(final int channelOrder) {
        switch(channelOrder) {
            case CL_R:
            case CL_A:
            case CL_INTENSITY:
            case CL_LUMINANCE:
            case CL_Rx:
                return 1;
            case CL_RG:
            case CL_RA:
            case CL_RGx:
                return 2;
            case CL_RGB:
            case CL_RGBx:
                return 3;
            case CL_RGBA:
            case CL_BGRA:
            case CL_ARGB:
                return 4;
            default:
                throw new IllegalArgumentException("Invalid cl_channel_order specified: " + LWJGLUtil.toHexString(channelOrder));
        }
    }
}
