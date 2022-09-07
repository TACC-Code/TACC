class BackupThread extends Thread {
    public static void drawgfx(osd_bitmap dest, GfxElement gfx, int code, int color, int flipx, int flipy, int sx, int sy, rectangle clip, int transparency, int transparent_color) {
        int ox, oy, ex, ey, x, y, start, dy;
        CharPtr sd = new CharPtr();
        CharPtr bm = new CharPtr();
        int col;
        rectangle myclip = new rectangle();
        if (gfx == null) {
            return;
        }
        if ((Machine.orientation & ORIENTATION_SWAP_XY) != 0) {
            int temp;
            temp = sx;
            sx = sy;
            sy = temp;
            temp = flipx;
            flipx = flipy;
            flipy = temp;
            if (clip != null) {
                myclip.min_x = clip.min_y;
                myclip.max_x = clip.max_y;
                myclip.min_y = clip.min_x;
                myclip.max_y = clip.max_x;
                clip = myclip;
            }
        }
        if ((Machine.orientation & ORIENTATION_FLIP_X) != 0) {
            sx = dest.width - gfx.width - sx;
            if (clip != null) {
                int temp;
                temp = clip.min_x;
                myclip.min_x = dest.width - 1 - clip.max_x;
                myclip.max_x = dest.width - 1 - temp;
                myclip.min_y = clip.min_y;
                myclip.max_y = clip.max_y;
                clip = myclip;
            }
        }
        if ((Machine.orientation & ORIENTATION_FLIP_Y) != 0) {
            int temp;
            sy = dest.height - gfx.height - sy;
            if (clip != null) {
                myclip.min_x = clip.min_x;
                myclip.max_x = clip.max_x;
                temp = clip.min_y;
                myclip.min_y = dest.height - 1 - clip.max_y;
                myclip.max_y = dest.height - 1 - temp;
                clip = myclip;
            }
        }
        ox = sx;
        oy = sy;
        ex = sx + gfx.width - 1;
        if (sx < 0) {
            sx = 0;
        }
        if (clip != null && sx < clip.min_x) {
            sx = clip.min_x;
        }
        if (ex >= dest.width) {
            ex = dest.width - 1;
        }
        if (clip != null && ex > clip.max_x) {
            ex = clip.max_x;
        }
        if (sx > ex) {
            return;
        }
        ey = sy + gfx.height - 1;
        if (sy < 0) {
            sy = 0;
        }
        if (clip != null && sy < clip.min_y) {
            sy = clip.min_y;
        }
        if (ey >= dest.height) {
            ey = dest.height - 1;
        }
        if (clip != null && ey > clip.max_y) {
            ey = clip.max_y;
        }
        if (sy > ey) {
            return;
        }
        if (flipy != 0) {
            start = (code % gfx.total_elements) * gfx.height + gfx.height - 1 - (sy - oy);
            dy = -1;
        } else {
            start = (code % gfx.total_elements) * gfx.height + (sy - oy);
            dy = 1;
        }
        if (transparency == TRANSPARENCY_COLOR || transparency == TRANSPARENCY_THROUGH) {
            transparent_color = Machine.pens[transparent_color];
        }
        if (gfx.colortable != null) {
            CharPtr paldata;
            paldata = new CharPtr(gfx.colortable, gfx.color_granularity * (color % gfx.total_colors));
            switch(transparency) {
                case TRANSPARENCY_NONE:
                    if (flipx != 0) {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], gfx.width - 1 - (sx - ox));
                            for (x = sx; x <= ex - 7; x += 8) {
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                                bm.writeinc(paldata.read(sd.readdec()));
                            }
                            for (; x <= ex; x++) {
                                bm.writeinc(paldata.read(sd.readdec()));
                            }
                            start += dy;
                        }
                    } else {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], (sx - ox));
                            for (x = sx; x <= ex - 7; x += 8) {
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                                bm.writeinc(paldata.read(sd.readinc()));
                            }
                            for (; x <= ex; x++) {
                                bm.writeinc(paldata.read(sd.readinc()));
                            }
                            start += dy;
                        }
                    }
                    break;
                case TRANSPARENCY_PEN:
                    if (flipx != 0) {
                        IntPtr sd4 = new IntPtr();
                        int trans4, col4;
                        trans4 = transparent_color * 0x01010101;
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd4.set(gfx.gfxdata.line[start], (gfx.width - 1 - (sx - ox) - 3));
                            for (x = sx; x <= ex - 3; x += 4) {
                                if ((col4 = sd4.read()) != trans4) {
                                    col = (col4 >> 24) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(0, paldata.read(col));
                                    }
                                    col = (col4 >> 16) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(1, paldata.read(col));
                                    }
                                    col = (col4 >> 8) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(2, paldata.read(col));
                                    }
                                    col = col4 & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(3, paldata.read(col));
                                    }
                                }
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                sd4.dec();
                            }
                            sd.set(sd4.readCA(), sd4.getBase() + 3);
                            for (; x <= ex; x++) {
                                col = sd.readdec();
                                if (col != transparent_color) {
                                    bm.write(col);
                                }
                                bm.inc();
                            }
                            start += dy;
                        }
                    } else {
                        IntPtr sd4 = new IntPtr();
                        int trans4, col4;
                        trans4 = transparent_color * 0x01010101;
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd4.set(gfx.gfxdata.line[start], (sx - ox));
                            for (x = sx; x <= ex - 3; x += 4) {
                                if ((col4 = sd4.read()) != trans4) {
                                    col = col4 & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(0, paldata.read(col));
                                    }
                                    col = (col4 >> 8) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(1, paldata.read(col));
                                    }
                                    col = (col4 >> 16) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(2, paldata.read(col));
                                    }
                                    col = (col4 >> 24) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(3, paldata.read(col));
                                    }
                                }
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                sd4.inc();
                            }
                            sd.set(sd4.readCA(), sd4.getBase());
                            for (; x <= ex; x++) {
                                col = sd.readinc();
                                if (col != transparent_color) {
                                    bm.write(col);
                                }
                                bm.inc();
                            }
                            start += dy;
                        }
                    }
                    break;
                case TRANSPARENCY_COLOR:
                    if (flipx != 0) {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], (gfx.width - 1 - (sx - ox)));
                            for (x = sx; x <= ex; x++) {
                                col = paldata.read(sd.readdec());
                                if (col != transparent_color) {
                                    bm.write(col);
                                }
                                bm.inc();
                            }
                            start += dy;
                        }
                    } else {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], (sx - ox));
                            for (x = sx; x <= ex; x++) {
                                col = paldata.read(sd.readinc());
                                if (col != transparent_color) {
                                    bm.write(col);
                                }
                                bm.inc();
                            }
                            start += dy;
                        }
                    }
                    break;
                case TRANSPARENCY_THROUGH:
                    if (flipx != 0) {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], gfx.width - 1 - (sx - ox));
                            for (x = sx; x <= ex; x++) {
                                if (bm.read() == transparent_color) {
                                    bm.write(paldata.read(sd.read()));
                                }
                                bm.inc();
                                sd.dec();
                            }
                            start += dy;
                        }
                    } else {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], (sx - ox));
                            for (x = sx; x <= ex; x++) {
                                if (bm.read() == transparent_color) {
                                    bm.write(paldata.read(sd.read()));
                                }
                                bm.inc();
                                sd.inc();
                            }
                            start += dy;
                        }
                    }
                    break;
            }
        } else {
            switch(transparency) {
                case TRANSPARENCY_NONE:
                    if (flipx != 0) {
                        System.out.println("B)TRANSPARENCY_NONE FLIPX *untested!");
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], gfx.width - 1 - (sx - ox));
                            for (x = sx; x <= ex - 7; x += 8) {
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                                bm.writeinc(sd.readdec());
                            }
                            for (; x <= ex; x++) {
                                bm.writeinc(sd.readdec());
                            }
                            start += dy;
                        }
                    } else {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], (sx - ox));
                            memcpy(bm, sd, ex - sx + 1);
                            start += dy;
                        }
                    }
                    break;
                case TRANSPARENCY_PEN:
                case TRANSPARENCY_COLOR:
                    if (flipx != 0) {
                        IntPtr sd4 = new IntPtr();
                        int trans4, col4;
                        trans4 = transparent_color * 0x01010101;
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd4.set(gfx.gfxdata.line[start], gfx.width - 1 - (sx - ox) - 3);
                            for (x = sx; x <= ex - 3; x += 4) {
                                if ((col4 = sd4.read()) != trans4) {
                                    col = col4 >> 24;
                                    if (col != transparent_color) {
                                        bm.write(0, col);
                                    }
                                    col = (col4 >> 16) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(1, col);
                                    }
                                    ;
                                    col = (col4 >> 8) & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(2, col);
                                    }
                                    ;
                                    col = col4 & 0xff;
                                    if (col != transparent_color) {
                                        bm.write(3, col);
                                    }
                                    ;
                                }
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                sd4.dec();
                            }
                            sd.set(sd4.readCA(), sd4.getBase() + 3);
                            for (; x <= ex; x++) {
                                col = sd.readdec();
                                if (col != transparent_color) {
                                    bm.write(col);
                                }
                                bm.inc();
                            }
                            start += dy;
                        }
                    } else {
                        IntPtr sd4 = new IntPtr();
                        int trans4, col4;
                        int xod4;
                        trans4 = transparent_color * 0x01010101;
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd4.set(gfx.gfxdata.line[start], (sx - ox));
                            for (x = sx; x <= ex - 3; x += 4) {
                                if ((col4 = sd4.read()) != trans4) {
                                    xod4 = col4 ^ trans4;
                                    if (((xod4 & 0x000000ff) != 0) && ((xod4 & 0x0000ff00) != 0) && ((xod4 & 0x00ff0000) != 0) && ((xod4 & 0xff000000) != 0)) {
                                        bm.write(0, col4 & 0xff);
                                        bm.write(1, (col4 >> 8) & 0xff);
                                        bm.write(2, (col4 >> 16) & 0xff);
                                        bm.write(3, (col4 >> 24) & 0xff);
                                    } else {
                                        if ((xod4 & 0x000000ff) != 0) {
                                            bm.write(0, col4 & 0xff);
                                        }
                                        if ((xod4 & 0x0000ff00) != 0) {
                                            bm.write(1, (col4 >> 8) & 0xff);
                                        }
                                        if ((xod4 & 0x00ff0000) != 0) {
                                            bm.write(2, (col4 >> 16) & 0xff);
                                        }
                                        if ((xod4 & 0xff000000) != 0) {
                                            bm.write(3, (col4 >> 24) & 0xff);
                                        }
                                    }
                                }
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                bm.inc();
                                sd4.inc();
                            }
                            sd.set(sd4.readCA(), sd4.getBase());
                            for (; x <= ex; x++) {
                                col = sd.readinc();
                                if (col != transparent_color) {
                                    bm.write(col);
                                }
                                bm.inc();
                            }
                            start += dy;
                        }
                    }
                    break;
                case TRANSPARENCY_THROUGH:
                    if (flipx != 0) {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], gfx.width - 1 - (sx - ox));
                            for (x = sx; x <= ex; x++) {
                                if (bm.read() == transparent_color) {
                                    bm.write(sd.read());
                                }
                                bm.inc();
                                sd.dec();
                            }
                            start += dy;
                        }
                    } else {
                        for (y = sy; y <= ey; y++) {
                            bm.set(dest.line[y], sx);
                            sd.set(gfx.gfxdata.line[start], (sx - ox));
                            for (x = sx; x <= ex; x++) {
                                if (bm.read() == transparent_color) {
                                    bm.write(sd.read());
                                }
                                bm.inc();
                                sd.inc();
                            }
                            start += dy;
                        }
                    }
                    break;
            }
        }
    }
}
