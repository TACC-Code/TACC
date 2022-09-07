class BackupThread extends Thread {
    private static String[] deriveShortCuts(int index, String[] words) {
        String base = words[index];
        int len = base.length();
        String[] res = new String[6];
        int w = 0;
        char c0 = base.charAt(0);
        if (Character.isUpperCase(c0)) {
            String s = base.toLowerCase();
            if (!Naming.isKeyword(s)) {
                res[w++] = s;
            }
        }
        if (len > 3) {
            char c1 = base.charAt(1);
            if (!isVowel(c1)) {
                String s = base.substring(0, 2).toLowerCase();
                if (!Naming.isKeyword(s)) {
                    res[w++] = s;
                }
            }
            if (!isVowel(c0)) {
                String bs = removeVowels(base);
                if (bs.length() == 2 || (bs.length() == 3 && len > 4)) {
                    String s = bs.toLowerCase();
                    if (!Naming.isKeyword(s)) {
                        res[w++] = s;
                    }
                }
            }
            if (len > 4) {
                char c2 = base.charAt(2);
                if (!isVowel(c2)) {
                    String s = base.substring(0, 3).toLowerCase();
                    if (!Naming.isKeyword(s)) {
                        res[w++] = s;
                    }
                }
            }
        }
        char lc0 = Character.toLowerCase(c0);
        if (len > 1 || Character.isUpperCase(c0)) {
            String s = "" + lc0;
            if (!Naming.isKeyword(s)) {
                res[w++] = s;
            }
        }
        for (int i = 1; i < w; i += 1) {
            String x = res[i];
            int j = i - 1;
            int xlen = x.length();
            while (j >= 0 && res[j].length() > xlen) {
                res[j + 1] = res[j];
                j -= 1;
            }
            if (j >= 0 && res[j].equals(x)) {
                for (j += 1, w -= 1, i -= 1; j < len; j += 1) {
                    res[j] = res[j + 1];
                }
            } else {
                res[j + 1] = x;
            }
        }
        String[] result = new String[w];
        for (int i = 0; i < w; i += 1) {
            result[i] = res[i];
        }
        return result;
    }
}
