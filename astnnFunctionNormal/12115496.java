class BackupThread extends Thread {
    private static int getChannelSize(final int channelType) {
        switch(channelType) {
            case CL_SNORM_INT8:
            case CL_UNORM_INT8:
            case CL_SIGNED_INT8:
            case CL_UNSIGNED_INT8:
                return 1;
            case CL_SNORM_INT16:
            case CL_UNORM_INT16:
            case CL_UNORM_SHORT_565:
            case CL_UNORM_SHORT_555:
            case CL_SIGNED_INT16:
            case CL_UNSIGNED_INT16:
            case CL_HALF_FLOAT:
                return 2;
            case CL_UNORM_INT_101010:
            case CL_SIGNED_INT32:
            case CL_UNSIGNED_INT32:
            case CL_FLOAT:
                return 4;
            default:
                throw new IllegalArgumentException("Invalid cl_channel_type specified: " + LWJGLUtil.toHexString(channelType));
        }
    }
}
