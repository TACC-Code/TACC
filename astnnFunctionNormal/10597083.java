class BackupThread extends Thread {
    public void doUpload(HttpServletRequest request) throws IOException {
        if (request.getContentType() == null) return;
        ServletInputStream in = request.getInputStream();
        OutputStream out = null;
        String buffer = null;
        byte[] line = new byte[256];
        int i = in.readLine(line, 0, 256);
        if (i < 3) return;
        int boundaryLength = i - 2;
        String boundary = new String(line, 0, boundaryLength);
        fields = new HashMap();
        while (i != -1) {
            String newLine = new String(line, 0, i);
            if (newLine.startsWith("Content-Disposition: form-data; name=\"")) {
                if ((newLine.indexOf("filename=\"") != -1) && this.isHasFile()) {
                    filename = newLine.substring((newLine.indexOf("filename=\"") + 10), newLine.lastIndexOf("\""));
                    if (filename.equalsIgnoreCase("") || filename == null) {
                        i = in.readLine(line, 0, 256);
                        continue;
                    }
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    i = in.readLine(line, 0, 256);
                    setContentType(new String(line, 0, (i - 2)));
                    i = in.readLine(line, 0, 256);
                    newLine = new String(line, 0, i);
                    int bytes_read;
                    String s_path;
                    if (temp_path == null) {
                        if (saveFilename == null) {
                            s_path = savePath + filename;
                        } else {
                            s_path = savePath + saveFilename + getExtension();
                        }
                    } else {
                        s_path = temp_path;
                    }
                    out = new FileOutputStream(s_path);
                    int total_read = 0;
                    int total_write = 0;
                    while ((bytes_read = in.readLine(line, 0, 256)) != -1 && !(newLine.indexOf(boundary) >= 0)) {
                        total_read += bytes_read;
                        newLine = new String(line, 0, bytes_read);
                        if (newLine.indexOf(boundary) >= 0) {
                            break;
                        } else if (total_read > this.fileSize) {
                            int to_write = this.fileSize - total_write;
                            byte[] last_line = new byte[to_write];
                            last_line = line;
                            out.write(last_line, 0, to_write);
                            break;
                        } else {
                            out.write(line, 0, bytes_read);
                            total_write += bytes_read;
                            newLine = new String(line, 0, bytes_read);
                        }
                    }
                    out.close();
                } else {
                    int pos = newLine.indexOf("name=\"");
                    String fieldName = newLine.substring(pos + 6, newLine.lastIndexOf("\""));
                    i = in.readLine(line, 0, 256);
                    i = in.readLine(line, 0, 256);
                    newLine = new String(line, 0, i);
                    StringBuffer fieldValue = new StringBuffer(256);
                    while (i != -1 && !newLine.startsWith(boundary)) {
                        i = in.readLine(line, 0, 256);
                        if ((i == boundaryLength + 2 || i == boundaryLength + 4) && (new String(line, 0, i).startsWith(boundary))) fieldValue.append(newLine.substring(0, newLine.length() - 2)); else fieldValue.append(newLine);
                        newLine = new String(line, 0, i);
                    }
                    fields.put(fieldName, fieldValue.toString());
                }
            }
            i = in.readLine(line, 0, 256);
        }
        in.close();
    }
}
