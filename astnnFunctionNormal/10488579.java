class BackupThread extends Thread {
    public static IPath fromPortableString(String pathString) {
        int firstMatch = pathString.indexOf(DEVICE_SEPARATOR) + 1;
        if (firstMatch <= 0) return new Path().initialize(null, pathString);
        String devicePart = null;
        int pathLength = pathString.length();
        if (firstMatch == pathLength || pathString.charAt(firstMatch) != DEVICE_SEPARATOR) {
            devicePart = pathString.substring(0, firstMatch);
            pathString = pathString.substring(firstMatch, pathLength);
        }
        if (pathString.indexOf(DEVICE_SEPARATOR) == -1) return new Path().initialize(devicePart, pathString);
        char[] chars = pathString.toCharArray();
        int readOffset = 0, writeOffset = 0, length = chars.length;
        while (readOffset < length) {
            if (chars[readOffset] == DEVICE_SEPARATOR) if (++readOffset >= length) break;
            chars[writeOffset++] = chars[readOffset++];
        }
        return new Path().initialize(devicePart, new String(chars, 0, writeOffset));
    }
}
