class BackupThread extends Thread {
    private int preprocess(BufferedReader reader, BufferedWriter writer, String[] excludeBeginMarkers, String[] excludeEndMarkers, String[] includeMarkers, String[] excludeFileMarkers) throws IOException {
        String line = null;
        boolean skip = false;
        int result = KEEP;
        String nextExcludeEndMarker = null;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            String trimmedLine = line.trim();
            if (isMarker(trimmedLine, excludeFileMarkers)) {
                return REMOVE;
            }
            if (!skip) {
                if (isMarker(trimmedLine, excludeBeginMarkers)) {
                    result = OVERWRITE;
                    skip = true;
                    nextExcludeEndMarker = getExcludeEndMarker(trimmedLine, excludeEndMarkers);
                } else {
                    if (!isMarker(trimmedLine, includeMarkers)) {
                        writer.write(line);
                        writer.newLine();
                    } else {
                        result = OVERWRITE;
                    }
                }
            } else {
                if (trimmedLine.startsWith(nextExcludeEndMarker)) {
                    skip = false;
                    nextExcludeEndMarker = null;
                }
            }
        }
        return result;
    }
}
