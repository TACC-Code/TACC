class BackupThread extends Thread {
    int proc(ZStream z, int r) {
        int t;
        int b;
        int k;
        int p;
        int n;
        int q;
        int m;
        {
            p = z.next_in_index;
            n = z.avail_in;
            b = bitb;
            k = bitk;
        }
        {
            q = write;
            m = (q < read ? read - q - 1 : end - q);
        }
        while (true) {
            switch(mode) {
                case TYPE:
                    while (k < 3) {
                        if (n != 0) {
                            r = Z_OK;
                        } else {
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        ;
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    t = (b & 7);
                    last = t & 1;
                    switch(t >>> 1) {
                        case 0:
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            t = k & 7;
                            {
                                b >>>= t;
                                k -= t;
                            }
                            mode = LENS;
                            break;
                        case 1:
                            {
                                int[] bl = new int[1];
                                int[] bd = new int[1];
                                int[][] tl = new int[1][];
                                int[][] td = new int[1][];
                                InfTree.inflate_trees_fixed(bl, bd, tl, td, z);
                                codes = new InfCodes(bl[0], bd[0], tl[0], td[0], z);
                            }
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            mode = CODES;
                            break;
                        case 2:
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            mode = TABLE;
                            break;
                        case 3:
                            {
                                b >>>= 3;
                                k -= 3;
                            }
                            mode = BAD;
                            z.msg = "invalid block type";
                            r = Z_DATA_ERROR;
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                    }
                    break;
                case LENS:
                    while (k < 32) {
                        if (n != 0) {
                            r = Z_OK;
                        } else {
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        ;
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    if ((~b >>> 16 & 0xffff) != (b & 0xffff)) {
                        mode = BAD;
                        z.msg = "invalid stored block lengths";
                        r = Z_DATA_ERROR;
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    left = b & 0xffff;
                    b = k = 0;
                    mode = left != 0 ? STORED : last != 0 ? DRY : TYPE;
                    break;
                case STORED:
                    if (n == 0) {
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    if (m == 0) {
                        if (q == end && read != 0) {
                            q = 0;
                            m = (q < read ? read - q - 1 : end - q);
                        }
                        if (m == 0) {
                            write = q;
                            r = inflate_flush(z, r);
                            q = write;
                            m = (q < read ? read - q - 1 : end - q);
                            if (q == end && read != 0) {
                                q = 0;
                                m = (q < read ? read - q - 1 : end - q);
                            }
                            if (m == 0) {
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                        }
                    }
                    r = Z_OK;
                    t = left;
                    if (t > n) {
                        t = n;
                    }
                    if (t > m) {
                        t = m;
                    }
                    System.arraycopy(z.next_in, p, window, q, t);
                    p += t;
                    n -= t;
                    q += t;
                    m -= t;
                    if ((left -= t) != 0) {
                        break;
                    }
                    mode = last != 0 ? DRY : TYPE;
                    break;
                case TABLE:
                    while (k < 14) {
                        if (n != 0) {
                            r = Z_OK;
                        } else {
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        ;
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    table = t = b & 0x3fff;
                    if ((t & 0x1f) > 29 || (t >> 5 & 0x1f) > 29) {
                        mode = BAD;
                        z.msg = "too many length or distance symbols";
                        r = Z_DATA_ERROR;
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    t = 258 + (t & 0x1f) + (t >> 5 & 0x1f);
                    blens = new int[t];
                    {
                        b >>>= 14;
                        k -= 14;
                    }
                    index = 0;
                    mode = BTREE;
                case BTREE:
                    while (index < 4 + (table >>> 10)) {
                        while (k < 3) {
                            if (n != 0) {
                                r = Z_OK;
                            } else {
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                            ;
                            n--;
                            b |= (z.next_in[p++] & 0xff) << k;
                            k += 8;
                        }
                        blens[border[index++]] = b & 7;
                        {
                            b >>>= 3;
                            k -= 3;
                        }
                    }
                    while (index < 19) {
                        blens[border[index++]] = 0;
                    }
                    bb[0] = 7;
                    t = InfTree.inflate_trees_bits(blens, bb, tb, hufts, z);
                    if (t != Z_OK) {
                        r = t;
                        if (r == Z_DATA_ERROR) {
                            blens = null;
                            mode = BAD;
                        }
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    index = 0;
                    mode = DTREE;
                case DTREE:
                    while (true) {
                        t = table;
                        if (!(index < 258 + (t & 0x1f) + (t >> 5 & 0x1f))) {
                            break;
                        }
                        int[] h;
                        int i, j, c;
                        t = bb[0];
                        while (k < t) {
                            if (n != 0) {
                                r = Z_OK;
                            } else {
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                            ;
                            n--;
                            b |= (z.next_in[p++] & 0xff) << k;
                            k += 8;
                        }
                        if (tb[0] == -1) {
                        }
                        t = hufts[(tb[0] + (b & inflate_mask[t])) * 3 + 1];
                        c = hufts[(tb[0] + (b & inflate_mask[t])) * 3 + 2];
                        if (c < 16) {
                            b >>>= t;
                            k -= t;
                            blens[index++] = c;
                        } else {
                            i = c == 18 ? 7 : c - 14;
                            j = c == 18 ? 11 : 3;
                            while (k < t + i) {
                                if (n != 0) {
                                    r = Z_OK;
                                } else {
                                    bitb = b;
                                    bitk = k;
                                    z.avail_in = n;
                                    z.total_in += p - z.next_in_index;
                                    z.next_in_index = p;
                                    write = q;
                                    return inflate_flush(z, r);
                                }
                                ;
                                n--;
                                b |= (z.next_in[p++] & 0xff) << k;
                                k += 8;
                            }
                            b >>>= t;
                            k -= t;
                            j += b & inflate_mask[i];
                            b >>>= i;
                            k -= i;
                            i = index;
                            t = table;
                            if (i + j > 258 + (t & 0x1f) + (t >> 5 & 0x1f) || c == 16 && i < 1) {
                                blens = null;
                                mode = BAD;
                                z.msg = "invalid bit length repeat";
                                r = Z_DATA_ERROR;
                                bitb = b;
                                bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                write = q;
                                return inflate_flush(z, r);
                            }
                            c = c == 16 ? blens[i - 1] : 0;
                            do {
                                blens[i++] = c;
                            } while (--j != 0);
                            index = i;
                        }
                    }
                    tb[0] = -1;
                    {
                        int[] bl = new int[1];
                        int[] bd = new int[1];
                        int[] tl = new int[1];
                        int[] td = new int[1];
                        InfCodes c;
                        bl[0] = 9;
                        bd[0] = 6;
                        t = table;
                        t = InfTree.inflate_trees_dynamic(257 + (t & 0x1f), 1 + (t >> 5 & 0x1f), blens, bl, bd, tl, td, hufts, z);
                        if (t != Z_OK) {
                            if (t == Z_DATA_ERROR) {
                                blens = null;
                                mode = BAD;
                            }
                            r = t;
                            bitb = b;
                            bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            write = q;
                            return inflate_flush(z, r);
                        }
                        codes = new InfCodes(bl[0], bd[0], hufts, tl[0], hufts, td[0], z);
                    }
                    blens = null;
                    mode = CODES;
                case CODES:
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    if ((r = codes.proc(this, z, r)) != Z_STREAM_END) {
                        return inflate_flush(z, r);
                    }
                    r = Z_OK;
                    codes.free(z);
                    p = z.next_in_index;
                    n = z.avail_in;
                    b = bitb;
                    k = bitk;
                    q = write;
                    m = (q < read ? read - q - 1 : end - q);
                    if (last == 0) {
                        mode = TYPE;
                        break;
                    }
                    mode = DRY;
                case DRY:
                    write = q;
                    r = inflate_flush(z, r);
                    q = write;
                    m = (q < read ? read - q - 1 : end - q);
                    if (read != write) {
                        bitb = b;
                        bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        write = q;
                        return inflate_flush(z, r);
                    }
                    mode = DONE;
                case DONE:
                    r = Z_STREAM_END;
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    return inflate_flush(z, r);
                case BAD:
                    r = Z_DATA_ERROR;
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    return inflate_flush(z, r);
                default:
                    r = Z_STREAM_ERROR;
                    bitb = b;
                    bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    write = q;
                    return inflate_flush(z, r);
            }
        }
    }
}
