class BackupThread extends Thread {
    private void readLav(File lav) throws Exception {
        if (singularvalues < 0) throw new Exception("Corrupt lao file! Couldn't find NSIG line before the singular values!");
        if (numterms <= 0) throw new Exception("Corrupt lao file! Couldn't find TERMS line before the singular values!");
        if (numdocs <= 0) throw new Exception("Corrupt lao file! Couldn't find DOCS line before the singular values!");
        FileInputStream reader = new FileInputStream(lav);
        int vVectors = 0;
        int uVectors = 0;
        int currentData = 0;
        int blocksize = 0;
        byte[] headerbuff = new byte[24];
        reader.read(headerbuff);
        rank = (int) arr2long(headerbuff, 0);
        lanczosSteps = arr2long(headerbuff, 8);
        kappa = arr2double(headerbuff, 16);
        byte[] buff = new byte[8];
        while ((blocksize = reader.read(buff)) != -1) {
            double num = arr2double(buff, 0);
            double exp = 0;
            String expSt = (new DecimalFormat("0.############E0")).format(num);
            if (expSt.split("E").length > 1) exp = Double.parseDouble(expSt.split("E")[1]);
            if (Math.abs(exp) > 10) {
                logger.debug(currentData + " Jumping: " + num);
                for (int i = 0; i < 7; i++) {
                    buff[i] = buff[i + 1];
                }
                buff[7] = (byte) reader.read();
                num = arr2double(buff, 0);
            }
            int index = currentData % singularvalues;
            int indexU = currentData % numterms;
            if (vVectors < numdocs) v.set(numdocs - vVectors - 1, index, num); else if (uVectors < singularvalues) {
                u.set(indexU, singularvalues - uVectors - 1, num);
            }
            if (blocksize != 8) logger.debug("Some size lost " + blocksize);
            currentData++;
            if (vVectors < numdocs) {
                if (currentData % singularvalues == 0) {
                    vVectors++;
                    if (vVectors == numdocs) currentData = 0;
                }
            } else {
                if (currentData % numterms == 0) uVectors++;
            }
        }
        v = Algebra.DEFAULT.transpose(v);
        logger.debug("Vectors:" + vVectors);
        reader.close();
    }
}
