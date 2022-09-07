class BackupThread extends Thread {
    public void processFile(BufferedReader reader, BufferedWriter writer) throws IOException, TranslationException {
        reset();
        char c;
        int i;
        FormatData fd = null;
        try {
            while ((i = getNextChar(reader)) >= 0) {
                c = (char) i;
                try {
                    if (c == '<') {
                        fd = null;
                        reader.mark(OConsts.READ_AHEAD_LIMIT);
                        handleTag(reader, writer);
                        continue;
                    }
                } catch (TranslationException e) {
                    reader.reset();
                }
                if (c == '\n' || c == 9 || c == ' ') {
                    if (!m_ws || fd == null) {
                        fd = new FormatData();
                        if (m_hasText) m_fdList.add(fd); else m_preNT.add(fd);
                    }
                    fd.appendOrig(c);
                    if (m_ws && !m_pre) continue;
                    m_ws = true;
                    if (m_pre) fd.appendDisplay(c); else fd.appendDisplay(' ');
                } else {
                    if (!m_hasText || m_ws || fd == null) {
                        fd = new FormatData();
                        m_fdList.add(fd);
                    }
                    fd.appendOrig(c);
                    if (c == '&') {
                        c = getEscChar(reader, fd);
                    }
                    if (c == 160 && !m_hasText) {
                        fd.appendDisplay(c);
                        m_ws = true;
                        m_hasText = false;
                    } else {
                        fd.appendDisplay(c);
                        fd.setHasText();
                        m_hasText = true;
                        m_ws = false;
                    }
                }
            }
            writeEntry(writer);
            writer.flush();
        } catch (IOException ioe) {
            String str = OStrings.FH_ERROR_WRITING_FILE;
            throw new IOException(str + ": \n" + ioe);
        }
    }
}
