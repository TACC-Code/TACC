class BackupThread extends Thread {
    public static void ConvertVideo() {
        Convertor.inpos = 0;
        Convertor.outpos = 0;
        boolean only_once_flag = false;
        boolean line_change_flag = false;
        int type = 0;
        int l = 0;
        int k = 0;
        label0: do {
            if (Convertor.inpos >= Convertor.inbuf.length) {
                break;
            }
            char c = sUtil.getChar();
            if (line_change_flag) {
                for (int i1 = 0; i1 < k; i1++) {
                    sUtil.putString("\t");
                }
                line_change_flag = false;
            }
            switch(c) {
                case 35:
                    if (!sUtil.getToken("#include")) {
                        break;
                    }
                    sUtil.skipLine();
                    if (!only_once_flag) {
                        only_once_flag = true;
                        sUtil.putString("/*\r\n");
                        sUtil.putString(" * ported to v" + Convertor.mameversion + "\r\n");
                        sUtil.putString(" * using automatic conversion tool v" + Convertor.convertorversion + "\r\n");
                        sUtil.putString(" * converted at : " + Convertor.timenow() + "\r\n");
                        sUtil.putString(" *\r\n");
                        sUtil.putString(" *\r\n");
                        sUtil.putString(" *\r\n");
                        sUtil.putString(" */ \r\n");
                        sUtil.putString("package vidhrdw;\r\n");
                        sUtil.putString("\r\n");
                        sUtil.putString((new StringBuilder()).append("public class ").append(Convertor.className).append("\r\n").toString());
                        sUtil.putString("{\r\n");
                        k = 1;
                        line_change_flag = true;
                    }
                    continue;
                case 10:
                    Convertor.outbuf[Convertor.outpos++] = Convertor.inbuf[Convertor.inpos++];
                    line_change_flag = true;
                    continue;
                case 45:
                    char c3 = sUtil.getNextChar();
                    if (c3 != '>') {
                        break;
                    }
                    Convertor.outbuf[Convertor.outpos++] = '.';
                    Convertor.inpos += 2;
                    continue;
                case 105:
                    int i = Convertor.inpos;
                    if (sUtil.getToken("if")) {
                        sUtil.skipSpace();
                        if (sUtil.parseChar() != '(') {
                            Convertor.inpos = i;
                            break;
                        }
                        sUtil.skipSpace();
                        Convertor.token[0] = sUtil.parseToken();
                        sUtil.skipSpace();
                        if (sUtil.getChar() == '&') {
                            Convertor.inpos++;
                            sUtil.skipSpace();
                            Convertor.token[1] = sUtil.parseToken();
                            sUtil.skipSpace();
                            Convertor.token[0] = (new StringBuilder()).append("(").append(Convertor.token[0]).append(" & ").append(Convertor.token[1]).append(")").toString();
                        }
                        if (sUtil.parseChar() != ')') {
                            Convertor.inpos = i;
                            break;
                        }
                        sUtil.putString((new StringBuilder()).append("if (").append(Convertor.token[0]).append(" != 0)").toString());
                        continue;
                    }
                    if (!sUtil.getToken("int")) {
                        break;
                    }
                    sUtil.skipSpace();
                    Convertor.token[0] = sUtil.parseToken();
                    sUtil.skipSpace();
                    if (sUtil.parseChar() != '(') {
                        Convertor.inpos = i;
                        break;
                    }
                    sUtil.skipSpace();
                    if (sUtil.getToken("void")) {
                        if (sUtil.parseChar() != ')') {
                            Convertor.inpos = i;
                            break;
                        }
                        if (Convertor.token[0].contains("vh_start")) {
                            sUtil.putString((new StringBuilder()).append("public static VhStartPtr ").append(Convertor.token[0]).append(" = new VhStartPtr() { public int handler() ").toString());
                            type = vh_start;
                            l = -1;
                            continue label0;
                        }
                    }
                    if (sUtil.getToken("int")) {
                        sUtil.skipSpace();
                        Convertor.token[1] = sUtil.parseToken();
                        sUtil.skipSpace();
                        if (sUtil.parseChar() != ')') {
                            Convertor.inpos = i;
                            break;
                        }
                        sUtil.skipSpace();
                        if (Convertor.token[0].length() > 0 && Convertor.token[1].length() > 0) {
                            sUtil.putString((new StringBuilder()).append("public static ReadHandlerPtr ").append(Convertor.token[0]).append(" = new ReadHandlerPtr() { public int handler(int ").append(Convertor.token[1]).append(")").toString());
                            type = vid_mem_read;
                            l = -1;
                            continue label0;
                        }
                    }
                    Convertor.inpos = i;
                    break;
                case 118:
                    int j = Convertor.inpos;
                    if (!sUtil.getToken("void")) {
                        break;
                    }
                    sUtil.skipSpace();
                    Convertor.token[0] = sUtil.parseToken();
                    sUtil.skipSpace();
                    if (sUtil.parseChar() != '(') {
                        Convertor.inpos = j;
                        break;
                    }
                    sUtil.skipSpace();
                    if (sUtil.getToken("struct osd_bitmap *bitmap")) {
                        if (sUtil.parseChar() != ')') {
                            Convertor.inpos = j;
                            break;
                        }
                        if (Convertor.token[0].contains("vh_screenrefresh")) {
                            sUtil.putString((new StringBuilder()).append("public static VhUpdatePtr ").append(Convertor.token[0]).append(" = new VhUpdatePtr() { public void handler(osd_bitmap bitmap) ").toString());
                            type = vh_screenrefresh;
                            l = -1;
                            continue label0;
                        }
                    }
                    if (sUtil.getToken("unsigned char *palette, unsigned char *colortable,const unsigned char *color_prom")) {
                        if (sUtil.parseChar() != ')') {
                            Convertor.inpos = j;
                            break;
                        }
                        if (Convertor.token[0].contains("vh_convert_color_prom")) {
                            sUtil.putString((new StringBuilder()).append("public static VhConvertColorPromPtr ").append(Convertor.token[0]).append(" = new VhConvertColorPromPtr() { public void handler(char []palette, char []colortable, char []color_prom) ").toString());
                            type = vh_convert;
                            l = -1;
                            continue label0;
                        }
                    }
                    if (sUtil.getToken("void")) {
                        if (sUtil.parseChar() != ')') {
                            Convertor.inpos = j;
                            break;
                        }
                        if (Convertor.token[0].contains("vh_stop")) {
                            sUtil.putString((new StringBuilder()).append("public static VhStopPtr ").append(Convertor.token[0]).append(" = new VhStopPtr() { public void handler() ").toString());
                            type = vh_stop;
                            l = -1;
                            continue label0;
                        }
                    }
                    if (!sUtil.getToken("int")) {
                        Convertor.inpos = j;
                        break;
                    }
                    sUtil.skipSpace();
                    Convertor.token[1] = sUtil.parseToken();
                    sUtil.skipSpace();
                    if (sUtil.parseChar() != ',') {
                        Convertor.inpos = j;
                        break;
                    }
                    sUtil.skipSpace();
                    if (!sUtil.getToken("int")) {
                        Convertor.inpos = j;
                        break;
                    }
                    sUtil.skipSpace();
                    Convertor.token[2] = sUtil.parseToken();
                    sUtil.skipSpace();
                    if (sUtil.parseChar() != ')') {
                        Convertor.inpos = j;
                        break;
                    }
                    sUtil.skipSpace();
                    if (Convertor.token[0].length() > 0 && Convertor.token[1].length() > 0 && Convertor.token[2].length() > 0) {
                        sUtil.putString((new StringBuilder()).append("public static WriteHandlerPtr ").append(Convertor.token[0]).append(" = new WriteHandlerPtr() { public void handler(int ").append(Convertor.token[1]).append(", int ").append(Convertor.token[2]).append(")").toString());
                        type = vid_mem_write;
                        l = -1;
                        continue label0;
                    }
                    Convertor.inpos = j;
                    break;
                case 123:
                    l++;
                    break;
                case 125:
                    l--;
                    if (type != vid_mem_read && type != vid_mem_write && type != vh_stop && type != vh_start && type != vh_screenrefresh && type != vh_convert || l != -1) {
                        break;
                    }
                    sUtil.putString("} };");
                    Convertor.inpos++;
                    type = -1;
                    continue;
            }
            Convertor.outbuf[Convertor.outpos++] = Convertor.inbuf[Convertor.inpos++];
        } while (true);
        if (only_once_flag) {
            sUtil.putString("}\r\n");
        }
    }
}
