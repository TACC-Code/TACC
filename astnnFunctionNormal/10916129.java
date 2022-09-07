class BackupThread extends Thread {
    private void handleTag(BufferedReader reader, BufferedWriter writer) throws IOException, TranslationException {
        HTMLTag tag;
        FormatData fd;
        tag = identTag(reader);
        fd = new FormatData(tag.close());
        fd.setOrig(tag.verbatum());
        if (tag.type() == HTMLTag.TAG_FORMAT) {
            if (m_hasText) {
                m_fdList.add(fd);
            } else {
                m_preNT.add(fd);
            }
            HTMLTag cand;
            if (tag.close()) {
                ListIterator it;
                it = m_tagList.listIterator(m_tagList.size());
                boolean foundPartner = false;
                while (it.hasPrevious()) {
                    cand = (HTMLTag) it.previous();
                    if (tag.willPartner(cand)) {
                        cand.setPartner();
                        tag.setPartner();
                        fd.setTagData(cand.shortcut(), cand.num());
                        foundPartner = true;
                        break;
                    }
                }
                if (!foundPartner) {
                    tag.setNum(m_tagList.size());
                    m_tagList.add(tag);
                    fd.setTagData(tag.shortcut(), tag.num());
                }
            } else {
                tag.setNum(m_tagList.size());
                m_tagList.add(tag);
                fd.setTagData(tag.shortcut(), tag.num());
            }
            fd.finalize();
        } else {
            m_postNT.add(fd);
            fd.finalize();
            writeEntry(writer);
        }
        if (tag.isPreTag()) {
            m_pre = !tag.close();
        }
    }
}
