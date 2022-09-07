class BackupThread extends Thread {
    public String transformString(String line) {
        String fields[] = line.split("\\s+");
        if (fields == null || (fields.length != 6 && fields.length != 4)) {
            String msg = "Invalid trec format : " + line;
            throw new IllegalArgumentException(msg);
        }
        if (fields.length == 4) {
            return line;
        }
        if (fields.length == 6) {
            String tmp = getDocno(MD5.digest(fields[2].trim()));
            if (tmp == null) {
                return line;
            }
            fields[2] = tmp;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(fields[0] + "\t");
        sb.append(fields[3] + "\t");
        sb.append(fields[4] + "\t");
        sb.append(fields[2]);
        return sb.toString();
    }
}
