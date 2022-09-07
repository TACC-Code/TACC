class BackupThread extends Thread {
    private void checkspace(int index, int length, String err) {
        if (length > 0) {
            Log.write("..added ", length);
            Log.write(" to index ", index);
            Log.writeln(", now ", tiles[index].usedSpace);
        }
        int max = usedSpaceStream.getMaxValue();
        if (tiles[index].usedSpace > max) {
            Log.write("Treadmill.traceObject: usedSpace too high at ", index);
            Log.write(": ", tiles[index].usedSpace);
            Log.write(", max=", max);
            Log.write(" in ");
            Log.writeln(err);
            tiles[index].usedSpace = max;
        }
    }
}
