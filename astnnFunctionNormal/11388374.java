class BackupThread extends Thread {
    private static int TEXT_NextLine(int hdc, int str, IntRef count, int dest, IntRef len, int width, int format, WinSize retsize, boolean last_line, IntRef p_retstr, int tabwidth, IntRef pprefix_offset, ellipsis_data pellip) {
        int i = 0, j = 0;
        int plen = 0;
        WinSize size = new WinSize();
        int maxl = len.value;
        int seg_i, seg_count, seg_j;
        int max_seg_width;
        int num_fit;
        int word_broken;
        boolean line_fits;
        IntRef j_in_seg = new IntRef(0);
        int ellipsified;
        int pNumFit = getTempBuffer(4);
        int pSize = getTempBuffer(WinSize.SIZE);
        pprefix_offset.value = -1;
        retsize.cy = 0;
        while (count.value != 0) {
            if (readb(str + i) == TAB && (format & DT_EXPANDTABS) != 0) {
                plen = ((plen / tabwidth) + 1) * tabwidth;
                count.value--;
                if (j < maxl) writeb(dest + j++, readb(str + i++)); else i++;
                while (count.value != 0 && readb(str + i) == TAB) {
                    plen += tabwidth;
                    count.value--;
                    if (j < maxl) writeb(dest + j++, readb(str + i++)); else i++;
                }
            }
            seg_i = i;
            seg_count = count.value;
            seg_j = j;
            while (count.value != 0 && (readb(str + i) != TAB || (format & DT_EXPANDTABS) == 0) && ((readb(str + i) != CR && readb(str + i) != LF) || (format & DT_SINGLELINE) != 0)) {
                if (readb(str + i) == PREFIX && (format & DT_NOPREFIX) == 0 && count.value > 1) {
                    count.value--;
                    i++;
                    if (readb(str + i) == PREFIX) {
                        count.value--;
                        if (j < maxl) writeb(dest + j++, readb(str + i++)); else i++;
                    } else if (pprefix_offset.value == -1 || pprefix_offset.value >= seg_j) {
                        pprefix_offset.value = j;
                    }
                } else {
                    count.value--;
                    if (j < maxl) writeb(dest + j++, readb(str + i++)); else i++;
                }
            }
            j_in_seg.value = j - seg_j;
            max_seg_width = width - plen;
            WinFont.GetTextExtentExPointA(hdc, dest + seg_j, j_in_seg.value, max_seg_width, pNumFit, NULL, pSize);
            num_fit = readd(pNumFit);
            size.copy(pSize);
            word_broken = 0;
            line_fits = (num_fit >= j_in_seg.value);
            if (!line_fits && (format & DT_WORDBREAK) != 0) {
                IntRef s = new IntRef(0);
                IntRef chars_used = new IntRef(0);
                TEXT_WordBreak(hdc, dest + seg_j, maxl - seg_j, j_in_seg, max_seg_width, format, num_fit, chars_used, size);
                line_fits = (size.cx <= max_seg_width);
                TEXT_SkipChars(count, s, seg_count, str + seg_i, i - seg_i, chars_used.value, (format & DT_NOPREFIX) == 0);
                i = s.value - str;
                word_broken = 1;
            }
            pellip.before = j_in_seg.value;
            pellip.under = 0;
            pellip.after = 0;
            pellip.len = 0;
            ellipsified = 0;
            if (!line_fits && (format & DT_PATH_ELLIPSIS) != 0) {
                TEXT_PathEllipsify(hdc, dest + seg_j, maxl - seg_j, j_in_seg, max_seg_width, size, p_retstr.value, pellip);
                line_fits = (size.cx <= max_seg_width);
                ellipsified = 1;
            }
            if ((!line_fits && (format & DT_WORD_ELLIPSIS) != 0) || ((format & DT_END_ELLIPSIS) != 0 && ((last_line && count.value != 0) || (remainder_is_none_or_newline(count.value, str + i) && !line_fits)))) {
                IntRef before = new IntRef(0);
                IntRef len_ellipsis = new IntRef(0);
                TEXT_Ellipsify(hdc, dest + seg_j, maxl - seg_j, j_in_seg, max_seg_width, size, p_retstr.value, before, len_ellipsis);
                if (before.value > pellip.before) {
                    pellip.after = before.value - pellip.before - pellip.len;
                } else {
                    assert (pellip.under == 0 && pellip.after == 0);
                    pellip.before = before.value;
                    pellip.len = len_ellipsis.value;
                }
                ellipsified = 1;
            }
            if ((format & DT_EXPANDTABS) != 0 && ellipsified != 0) {
                if ((format & DT_SINGLELINE) != 0) count.value = 0; else {
                    while (count.value != 0 && readb(str + i) != CR && readb(str + i) != LF) {
                        count.value--;
                        i++;
                    }
                }
            }
            j = seg_j + j_in_seg.value;
            if (pprefix_offset.value >= seg_j + pellip.before) {
                pprefix_offset.value = TEXT_Reprefix(str + seg_i, i - seg_i, pellip);
                if (pprefix_offset.value != -1) pprefix_offset.value += seg_j;
            }
            plen += size.cx;
            if (size.cy > retsize.cy) retsize.cy = size.cy;
            if (word_broken != 0) break; else if (count.value == 0) break; else if (readb(str + i) == CR || readb(str + i) == LF) {
                count.value--;
                i++;
                if (count.value != 0 && (readb(str + i) == CR || readb(str + i) == LF) && readb(str + i) != readb(str + i - 1)) {
                    count.value--;
                    i++;
                }
                break;
            }
        }
        retsize.cx = plen;
        len.value = j;
        if (count.value != 0) return str + i; else return NULL;
    }
}
