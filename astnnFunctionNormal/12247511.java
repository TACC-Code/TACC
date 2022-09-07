class BackupThread extends Thread {
    private static String[] parsePattern(final String[] args, final String... commands) {
        final int ptnlen = commands.length;
        final String[] result = new String[ptnlen];
        int ri = 0;
        final int arglen = args.length;
        for (int i = COMMAND.length; i + 1 < arglen; i += 2) {
            final String s = args[i];
            final int pi = ArrayUtils.indexOf(commands, s, ri);
            if (pi == ArrayUtils.INDEX_NOT_FOUND) {
                continue;
            }
            result[pi] = args[i + 1];
            ri = pi + 1;
            if (ri >= ptnlen) {
                break;
            }
        }
        return result;
    }
}
