class BackupThread extends Thread {
    public static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, int srcRedMask, int srcGreenMask, int srcBlueMask, int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, int destRedMask, int destGreenMask, int destBlueMask, boolean flipX, boolean flipY) {
        if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;
        final int srcAlphaMask = 0, destAlphaMask = 0;
        final int dwm1 = destWidth - 1;
        final int sfxi = (dwm1 != 0) ? (int) ((((long) srcWidth << 16) - 1) / dwm1) : 0;
        final int dhm1 = destHeight - 1;
        final int sfyi = (dhm1 != 0) ? (int) ((((long) srcHeight << 16) - 1) / dhm1) : 0;
        final int sbpp, stype;
        switch(srcDepth) {
            case 8:
                sbpp = 1;
                stype = TYPE_GENERIC_8;
                break;
            case 16:
                sbpp = 2;
                stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
                break;
            case 24:
                sbpp = 3;
                stype = TYPE_GENERIC_24;
                break;
            case 32:
                sbpp = 4;
                stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
                break;
            default:
                return;
        }
        int spr = srcY * srcStride + srcX * sbpp;
        final int dbpp, dtype;
        switch(destDepth) {
            case 8:
                dbpp = 1;
                dtype = TYPE_GENERIC_8;
                break;
            case 16:
                dbpp = 2;
                dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
                break;
            case 24:
                dbpp = 3;
                dtype = TYPE_GENERIC_24;
                break;
            case 32:
                dbpp = 4;
                dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
                break;
            default:
                return;
        }
        int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX) * dbpp;
        final int dprxi = (flipX) ? -dbpp : dbpp;
        final int dpryi = (flipY) ? -destStride : destStride;
        int apr;
        if ((op & BLIT_ALPHA) != 0) {
            switch(alphaMode) {
                case ALPHA_MASK_UNPACKED:
                case ALPHA_CHANNEL_SEPARATE:
                    if (alphaData == null) alphaMode = 0x10000;
                    apr = alphaY * alphaStride + alphaX;
                    break;
                case ALPHA_MASK_PACKED:
                    if (alphaData == null) alphaMode = 0x10000;
                    alphaStride <<= 3;
                    apr = alphaY * alphaStride + alphaX;
                    break;
                case ALPHA_MASK_INDEX:
                    return;
                case ALPHA_MASK_RGB:
                    if (alphaData == null) alphaMode = 0x10000;
                    apr = 0;
                    break;
                default:
                    alphaMode = (alphaMode << 16) / 255;
                case ALPHA_CHANNEL_SOURCE:
                    apr = 0;
                    break;
            }
        } else {
            alphaMode = 0x10000;
            apr = 0;
        }
        int dp = dpr;
        int sp = spr;
        if ((alphaMode == 0x10000) && (stype == dtype) && (srcRedMask == destRedMask) && (srcGreenMask == destGreenMask) && (srcBlueMask == destBlueMask) && (srcAlphaMask == destAlphaMask)) {
            switch(sbpp) {
                case 1:
                    for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
                        for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
                            destData[dp] = srcData[sp];
                            sp += (sfx >>> 16);
                        }
                    }
                    break;
                case 2:
                    for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
                        for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
                            destData[dp] = srcData[sp];
                            destData[dp + 1] = srcData[sp + 1];
                            sp += (sfx >>> 16) * 2;
                        }
                    }
                    break;
                case 3:
                    for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
                        for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
                            destData[dp] = srcData[sp];
                            destData[dp + 1] = srcData[sp + 1];
                            destData[dp + 2] = srcData[sp + 2];
                            sp += (sfx >>> 16) * 3;
                        }
                    }
                    break;
                case 4:
                    for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
                        for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
                            destData[dp] = srcData[sp];
                            destData[dp + 1] = srcData[sp + 1];
                            destData[dp + 2] = srcData[sp + 2];
                            destData[dp + 3] = srcData[sp + 3];
                            sp += (sfx >>> 16) * 4;
                        }
                    }
                    break;
            }
            return;
        }
        final int srcRedShift = getChannelShift(srcRedMask);
        final byte[] srcReds = ANY_TO_EIGHT[getChannelWidth(srcRedMask, srcRedShift)];
        final int srcGreenShift = getChannelShift(srcGreenMask);
        final byte[] srcGreens = ANY_TO_EIGHT[getChannelWidth(srcGreenMask, srcGreenShift)];
        final int srcBlueShift = getChannelShift(srcBlueMask);
        final byte[] srcBlues = ANY_TO_EIGHT[getChannelWidth(srcBlueMask, srcBlueShift)];
        final int srcAlphaShift = getChannelShift(srcAlphaMask);
        final byte[] srcAlphas = ANY_TO_EIGHT[getChannelWidth(srcAlphaMask, srcAlphaShift)];
        final int destRedShift = getChannelShift(destRedMask);
        final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
        final byte[] destReds = ANY_TO_EIGHT[destRedWidth];
        final int destRedPreShift = 8 - destRedWidth;
        final int destGreenShift = getChannelShift(destGreenMask);
        final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
        final byte[] destGreens = ANY_TO_EIGHT[destGreenWidth];
        final int destGreenPreShift = 8 - destGreenWidth;
        final int destBlueShift = getChannelShift(destBlueMask);
        final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
        final byte[] destBlues = ANY_TO_EIGHT[destBlueWidth];
        final int destBluePreShift = 8 - destBlueWidth;
        final int destAlphaShift = getChannelShift(destAlphaMask);
        final int destAlphaWidth = getChannelWidth(destAlphaMask, destAlphaShift);
        final byte[] destAlphas = ANY_TO_EIGHT[destAlphaWidth];
        final int destAlphaPreShift = 8 - destAlphaWidth;
        int ap = apr, alpha = alphaMode;
        int r = 0, g = 0, b = 0, a = 0;
        int rq = 0, gq = 0, bq = 0, aq = 0;
        for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, ap = apr += (sfy >>> 16) * alphaStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
            for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
                switch(stype) {
                    case TYPE_GENERIC_8:
                        {
                            final int data = srcData[sp] & 0xff;
                            sp += (sfx >>> 16);
                            r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
                            g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
                            b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
                            a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
                        }
                        break;
                    case TYPE_GENERIC_16_MSB:
                        {
                            final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
                            sp += (sfx >>> 16) * 2;
                            r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
                            g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
                            b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
                            a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
                        }
                        break;
                    case TYPE_GENERIC_16_LSB:
                        {
                            final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
                            sp += (sfx >>> 16) * 2;
                            r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
                            g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
                            b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
                            a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
                        }
                        break;
                    case TYPE_GENERIC_24:
                        {
                            final int data = ((((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff)) << 8) | (srcData[sp + 2] & 0xff);
                            sp += (sfx >>> 16) * 3;
                            r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
                            g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
                            b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
                            a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
                        }
                        break;
                    case TYPE_GENERIC_32_MSB:
                        {
                            final int data = ((((((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff)) << 8) | (srcData[sp + 2] & 0xff)) << 8) | (srcData[sp + 3] & 0xff);
                            sp += (sfx >>> 16) * 4;
                            r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
                            g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
                            b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
                            a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
                        }
                        break;
                    case TYPE_GENERIC_32_LSB:
                        {
                            final int data = ((((((srcData[sp + 3] & 0xff) << 8) | (srcData[sp + 2] & 0xff)) << 8) | (srcData[sp + 1] & 0xff)) << 8) | (srcData[sp] & 0xff);
                            sp += (sfx >>> 16) * 4;
                            r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
                            g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
                            b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
                            a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
                        }
                        break;
                }
                switch(alphaMode) {
                    case ALPHA_CHANNEL_SEPARATE:
                        alpha = ((alphaData[ap] & 0xff) << 16) / 255;
                        ap += (sfx >> 16);
                        break;
                    case ALPHA_CHANNEL_SOURCE:
                        alpha = (a << 16) / 255;
                        break;
                    case ALPHA_MASK_UNPACKED:
                        alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
                        ap += (sfx >> 16);
                        break;
                    case ALPHA_MASK_PACKED:
                        alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
                        ap += (sfx >> 16);
                        break;
                    case ALPHA_MASK_RGB:
                        alpha = 0x10000;
                        for (int i = 0; i < alphaData.length; i += 3) {
                            if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) {
                                alpha = 0x0000;
                                break;
                            }
                        }
                        break;
                }
                if (alpha != 0x10000) {
                    if (alpha == 0x0000) continue;
                    switch(dtype) {
                        case TYPE_GENERIC_8:
                            {
                                final int data = destData[dp] & 0xff;
                                rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
                                gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
                                bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
                                aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
                            }
                            break;
                        case TYPE_GENERIC_16_MSB:
                            {
                                final int data = ((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff);
                                rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
                                gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
                                bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
                                aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
                            }
                            break;
                        case TYPE_GENERIC_16_LSB:
                            {
                                final int data = ((destData[dp + 1] & 0xff) << 8) | (destData[dp] & 0xff);
                                rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
                                gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
                                bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
                                aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
                            }
                            break;
                        case TYPE_GENERIC_24:
                            {
                                final int data = ((((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff)) << 8) | (destData[dp + 2] & 0xff);
                                rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
                                gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
                                bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
                                aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
                            }
                            break;
                        case TYPE_GENERIC_32_MSB:
                            {
                                final int data = ((((((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff)) << 8) | (destData[dp + 2] & 0xff)) << 8) | (destData[dp + 3] & 0xff);
                                rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
                                gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
                                bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
                                aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
                            }
                            break;
                        case TYPE_GENERIC_32_LSB:
                            {
                                final int data = ((((((destData[dp + 3] & 0xff) << 8) | (destData[dp + 2] & 0xff)) << 8) | (destData[dp + 1] & 0xff)) << 8) | (destData[dp] & 0xff);
                                rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
                                gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
                                bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
                                aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
                            }
                            break;
                    }
                    a = aq + ((a - aq) * alpha >> 16);
                    r = rq + ((r - rq) * alpha >> 16);
                    g = gq + ((g - gq) * alpha >> 16);
                    b = bq + ((b - bq) * alpha >> 16);
                }
                final int data = (r >>> destRedPreShift << destRedShift) | (g >>> destGreenPreShift << destGreenShift) | (b >>> destBluePreShift << destBlueShift) | (a >>> destAlphaPreShift << destAlphaShift);
                switch(dtype) {
                    case TYPE_GENERIC_8:
                        {
                            destData[dp] = (byte) data;
                        }
                        break;
                    case TYPE_GENERIC_16_MSB:
                        {
                            destData[dp] = (byte) (data >>> 8);
                            destData[dp + 1] = (byte) (data & 0xff);
                        }
                        break;
                    case TYPE_GENERIC_16_LSB:
                        {
                            destData[dp] = (byte) (data & 0xff);
                            destData[dp + 1] = (byte) (data >>> 8);
                        }
                        break;
                    case TYPE_GENERIC_24:
                        {
                            destData[dp] = (byte) (data >>> 16);
                            destData[dp + 1] = (byte) (data >>> 8);
                            destData[dp + 2] = (byte) (data & 0xff);
                        }
                        break;
                    case TYPE_GENERIC_32_MSB:
                        {
                            destData[dp] = (byte) (data >>> 24);
                            destData[dp + 1] = (byte) (data >>> 16);
                            destData[dp + 2] = (byte) (data >>> 8);
                            destData[dp + 3] = (byte) (data & 0xff);
                        }
                        break;
                    case TYPE_GENERIC_32_LSB:
                        {
                            destData[dp] = (byte) (data & 0xff);
                            destData[dp + 1] = (byte) (data >>> 8);
                            destData[dp + 2] = (byte) (data >>> 16);
                            destData[dp + 3] = (byte) (data >>> 24);
                        }
                        break;
                }
            }
        }
    }
}
