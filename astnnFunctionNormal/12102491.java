class BackupThread extends Thread {
    public static void print(Object x, Writer w) throws Exception {
        if (PRINT_INITIALIZED.isBound() && RT.booleanCast(PRINT_INITIALIZED.deref())) PR_ON.invoke(x, w); else {
            boolean readably = booleanCast(PRINT_READABLY.deref());
            if (x instanceof Obj) {
                Obj o = (Obj) x;
                if (RT.count(o.meta()) > 0 && ((readably && booleanCast(PRINT_META.deref())) || booleanCast(PRINT_DUP.deref()))) {
                    IPersistentMap meta = o.meta();
                    w.write("#^");
                    if (meta.count() == 1 && meta.containsKey(TAG_KEY)) print(meta.valAt(TAG_KEY), w); else print(meta, w);
                    w.write(' ');
                }
            }
            if (x == null) w.write("nil"); else if (x instanceof ISeq || x instanceof IPersistentList) {
                w.write('(');
                printInnerSeq(seq(x), w);
                w.write(')');
            } else if (x instanceof String) {
                String s = (String) x;
                if (!readably) w.write(s); else {
                    w.write('"');
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        switch(c) {
                            case '\n':
                                w.write("\\n");
                                break;
                            case '\t':
                                w.write("\\t");
                                break;
                            case '\r':
                                w.write("\\r");
                                break;
                            case '"':
                                w.write("\\\"");
                                break;
                            case '\\':
                                w.write("\\\\");
                                break;
                            case '\f':
                                w.write("\\f");
                                break;
                            case '\b':
                                w.write("\\b");
                                break;
                            default:
                                w.write(c);
                        }
                    }
                    w.write('"');
                }
            } else if (x instanceof IPersistentMap) {
                w.write('{');
                for (ISeq s = seq(x); s != null; s = s.next()) {
                    IMapEntry e = (IMapEntry) s.first();
                    print(e.key(), w);
                    w.write(' ');
                    print(e.val(), w);
                    if (s.next() != null) w.write(", ");
                }
                w.write('}');
            } else if (x instanceof IPersistentVector) {
                IPersistentVector a = (IPersistentVector) x;
                w.write('[');
                for (int i = 0; i < a.count(); i++) {
                    print(a.nth(i), w);
                    if (i < a.count() - 1) w.write(' ');
                }
                w.write(']');
            } else if (x instanceof IPersistentSet) {
                w.write("#{");
                for (ISeq s = seq(x); s != null; s = s.next()) {
                    print(s.first(), w);
                    if (s.next() != null) w.write(" ");
                }
                w.write('}');
            } else if (x instanceof Character) {
                char c = ((Character) x).charValue();
                if (!readably) w.write(c); else {
                    w.write('\\');
                    switch(c) {
                        case '\n':
                            w.write("newline");
                            break;
                        case '\t':
                            w.write("tab");
                            break;
                        case ' ':
                            w.write("space");
                            break;
                        case '\b':
                            w.write("backspace");
                            break;
                        case '\f':
                            w.write("formfeed");
                            break;
                        case '\r':
                            w.write("return");
                            break;
                        default:
                            w.write(c);
                    }
                }
            } else if (x instanceof Class) {
                w.write("#=");
                w.write(((Class) x).getName());
            } else if (x instanceof BigDecimal && readably) {
                w.write(x.toString());
                w.write('M');
            } else if (x instanceof Var) {
                Var v = (Var) x;
                w.write("#=(var " + v.ns.name + "/" + v.sym + ")");
            } else w.write(x.toString());
        }
    }
}
