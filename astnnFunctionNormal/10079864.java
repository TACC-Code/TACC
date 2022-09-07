class BackupThread extends Thread {
    public String getCipherString(String source) throws CadenaNoCifradaException {
        try {
            if (source != null && !source.trim().equals("")) {
                MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_MD5);
                byte[] bs;
                messageDigest.reset();
                bs = messageDigest.digest(source.getBytes());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < bs.length; i++) {
                    String hexVal = Integer.toHexString(0xFF & bs[i]);
                    if (hexVal.length() == 1) {
                        stringBuilder.append("0");
                    }
                    stringBuilder.append(hexVal);
                }
                return stringBuilder.toString();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new CadenaNoCifradaException("La cadena no debe ser nula o vacia", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CadenaNoCifradaException("La cadena no pudo ser cifrada", e);
        }
    }
}
