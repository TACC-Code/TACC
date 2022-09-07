class BackupThread extends Thread {
    public static String reasonToString(int reason) {
        String ret = null;
        switch(reason) {
            case NOT_READABLE_FILE:
                ret = new String("Can not read the split file");
                break;
            case NOT_EXISTING_FILE:
                ret = new String("The split file doesn't exist");
                break;
            case FILE_IS_DIRECTORY:
                ret = new String("The split file is a directory");
                break;
            case FILE_NOT_FOUND:
                ret = new String("The split file has not been found");
                break;
            case FILE_EMPTY:
                ret = new String("The split file is empty");
                break;
            case IOEXCEPTION_READING:
                ret = new String("Error reading on file to split");
                break;
            case IOEXCEPTION_WRITTING:
                ret = new String("Error writting on chunk file");
                break;
            case SPLITFILE_SMALLER:
                ret = new String("File to split is smaller than expected");
                break;
            case SPLITTING_CANCELLED:
                ret = new String("Splitting has been cancelled");
                break;
            case INTERNAL_ERROR1:
                ret = new String("Internal error: there are more bytes to read than to write");
                break;
            case INTERNAL_ERROR2:
                ret = new String("Internal error: there are less bytes to read than to write");
                break;
            case INTERNAL_ERROR3:
                ret = new String("Internal error: thread interrupted");
                break;
            default:
                ret = new String("Unknown error");
                break;
        }
        return ret;
    }
}
