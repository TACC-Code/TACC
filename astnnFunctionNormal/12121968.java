class BackupThread extends Thread {
    public void setrideniPolozek(String[] pol) {
        int i[] = new int[11];
        int j[] = new int[3];
        int n;
        for (n = 0; n <= 2; n++) {
            if (pol[n].equals("a")) {
                i[n] = 1;
                j[n] = 1;
            } else if (pol[n].equals("b")) {
                i[n] = 1;
                j[n] = 2;
            } else if (pol[n].equals("c")) {
                i[n] = 1;
                j[n] = 3;
            } else if (pol[n].equals("alfa")) {
                i[n] = 2;
                j[n] = 1;
            } else if (pol[n].equals("beta")) {
                i[n] = 2;
                j[n] = 2;
            } else if (pol[n].equals("gama")) {
                i[n] = 2;
                j[n] = 3;
            } else if (pol[n].equals("va")) {
                i[n] = 3;
                j[n] = 1;
            } else if (pol[n].equals("vb")) {
                i[n] = 3;
                j[n] = 2;
            } else if (pol[n].equals("vc")) {
                i[n] = 3;
                j[n] = 3;
            } else if (pol[n].equals("ta")) {
                i[n] = 4;
                j[n] = 1;
            } else if (pol[n].equals("tb")) {
                i[n] = 4;
                j[n] = 2;
            } else if (pol[n].equals("tc")) {
                i[n] = 4;
                j[n] = 3;
            } else if (pol[n].equals("ua")) {
                i[n] = 5;
                j[n] = 1;
            } else if (pol[n].equals("ub")) {
                i[n] = 5;
                j[n] = 2;
            } else if (pol[n].equals("uc")) {
                i[n] = 5;
                j[n] = 3;
            } else if (pol[n].equals("p")) i[n] = 6; else if (pol[n].equals("r")) i[n] = 7; else if (pol[n].equals("ro")) i[n] = 8; else if (pol[n].equals("ab")) i[n] = 9; else if (pol[n].equals("ob")) i[n] = 10; else if (pol[n].equals("z")) i[n] = 11;
        }
        int d = 0;
        if (i[2] == 9 || i[1] == 9 || i[0] > 5 || i[2] == 11) d = 0; else {
            if (i[0] == i[1]) {
                if (j[0] == 1) {
                    if (j[1] == 2) d = 0; else if (j[1] == 3) d = 2;
                } else d = 1;
            } else d = j[0] - 1;
        }
        if (i[0] < 6 && d > 0) {
            zadaniHlaseni("CHANGE");
        }
        for (n = 0; n <= 2; n++) {
            if (i[n] > 5) continue;
            j[n] = j[n] - d;
            if (j[n] < 1) j[n] = j[n] + 3;
        }
        for (n = 0; n <= 1; n++) {
            if (i[n] == i[n + 1]) {
                if (j[n] > j[n + 1]) {
                    int m = j[n];
                    j[n] = j[n + 1];
                    j[n + 1] = m;
                }
            }
        }
        for (n = 0; n <= 2; n++) {
            if (i[n] == 1 && j[n] == 1) pol[n] = "a"; else if (i[n] == 1 && j[n] == 2) pol[n] = "b"; else if (i[n] == 1 && j[n] == 3) pol[n] = "c"; else if (i[n] == 2 && j[n] == 1) pol[n] = "alfa"; else if (i[n] == 2 && j[n] == 2) pol[n] = "beta"; else if (i[n] == 2 && j[n] == 3) pol[n] = "gama"; else if (i[n] == 3 && j[n] == 1) pol[n] = "va"; else if (i[n] == 3 && j[n] == 2) pol[n] = "vb"; else if (i[n] == 3 && j[n] == 3) pol[n] = "vc"; else if (i[n] == 4 && j[n] == 1) pol[n] = "ta"; else if (i[n] == 4 && j[n] == 2) pol[n] = "tb"; else if (i[n] == 4 && j[n] == 3) pol[n] = "tc"; else if (i[n] == 5 && j[n] == 1) pol[n] = "ua"; else if (i[n] == 5 && j[n] == 2) pol[n] = "ub"; else if (i[n] == 5 && j[n] == 3) pol[n] = "uc"; else if (i[n] == 6) pol[n] = "p"; else if (i[n] == 7) pol[n] = "r"; else if (i[n] == 8) pol[n] = "ro"; else if (i[n] == 9) pol[n] = "ab"; else if (i[n] == 10) pol[n] = "ob"; else if (i[n] == 11) pol[n] = "z";
        }
    }
}
