class BackupThread extends Thread {
    private static long _computeSerialVersionUID(Class cl) {
        if (DEBUG_SVUID) msg("Computing SerialVersionUID for " + cl);
        ByteArrayOutputStream devnull = new ByteArrayOutputStream(512);
        long h = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DigestOutputStream mdo = new DigestOutputStream(devnull, md);
            DataOutputStream data = new DataOutputStream(mdo);
            if (DEBUG_SVUID) msg("\twriteUTF( \"" + cl.getName() + "\" )");
            data.writeUTF(cl.getName());
            int classaccess = cl.getModifiers();
            classaccess &= (Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT);
            Method[] method = cl.getDeclaredMethods();
            if ((classaccess & Modifier.INTERFACE) != 0) {
                classaccess &= (~Modifier.ABSTRACT);
                if (method.length > 0) {
                    classaccess |= Modifier.ABSTRACT;
                }
            }
            classaccess &= CLASS_MASK;
            if (DEBUG_SVUID) msg("\twriteInt( " + classaccess + " ) ");
            data.writeInt(classaccess);
            if (!cl.isArray()) {
                Class interfaces[] = cl.getInterfaces();
                Arrays.sort(interfaces, compareClassByName);
                for (int i = 0; i < interfaces.length; i++) {
                    if (DEBUG_SVUID) msg("\twriteUTF( \"" + interfaces[i].getName() + "\" ) ");
                    data.writeUTF(interfaces[i].getName());
                }
            }
            Field[] field = cl.getDeclaredFields();
            Arrays.sort(field, compareMemberByName);
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                int m = f.getModifiers();
                if (Modifier.isPrivate(m) && (Modifier.isTransient(m) || Modifier.isStatic(m))) continue;
                if (DEBUG_SVUID) msg("\twriteUTF( \"" + f.getName() + "\" ) ");
                data.writeUTF(f.getName());
                m &= FIELD_MASK;
                if (DEBUG_SVUID) msg("\twriteInt( " + m + " ) ");
                data.writeInt(m);
                if (DEBUG_SVUID) msg("\twriteUTF( \"" + getSignature(f.getType()) + "\" ) ");
                data.writeUTF(getSignature(f.getType()));
            }
            if (hasStaticInitializer(cl)) {
                if (DEBUG_SVUID) msg("\twriteUTF( \"<clinit>\" ) ");
                data.writeUTF("<clinit>");
                if (DEBUG_SVUID) msg("\twriteInt( " + Modifier.STATIC + " )");
                data.writeInt(Modifier.STATIC);
                if (DEBUG_SVUID) msg("\twriteUTF( \"()V\" )");
                data.writeUTF("()V");
            }
            MethodSignature[] constructors = MethodSignature.removePrivateAndSort(cl.getDeclaredConstructors());
            for (int i = 0; i < constructors.length; i++) {
                MethodSignature c = constructors[i];
                String mname = "<init>";
                String desc = c.signature;
                desc = desc.replace('/', '.');
                if (DEBUG_SVUID) msg("\twriteUTF( \"" + mname + "\" )");
                data.writeUTF(mname);
                int modifier = c.member.getModifiers() & METHOD_MASK;
                if (DEBUG_SVUID) msg("\twriteInt( " + modifier + " ) ");
                data.writeInt(modifier);
                if (DEBUG_SVUID) msg("\twriteUTF( \"" + desc + "\" )");
                data.writeUTF(desc);
            }
            MethodSignature[] methods = MethodSignature.removePrivateAndSort(method);
            for (int i = 0; i < methods.length; i++) {
                MethodSignature m = methods[i];
                String desc = m.signature;
                desc = desc.replace('/', '.');
                if (DEBUG_SVUID) msg("\twriteUTF( \"" + m.member.getName() + "\" )");
                data.writeUTF(m.member.getName());
                int modifier = m.member.getModifiers() & METHOD_MASK;
                if (DEBUG_SVUID) msg("\twriteInt( " + modifier + " ) ");
                data.writeInt(modifier);
                if (DEBUG_SVUID) msg("\twriteUTF( \"" + desc + "\" )");
                data.writeUTF(desc);
            }
            data.flush();
            byte hasharray[] = md.digest();
            for (int i = 0; i < Math.min(8, hasharray.length); i++) {
                h += (long) (hasharray[i] & 255) << (i * 8);
            }
        } catch (IOException ignore) {
            h = -1;
        } catch (NoSuchAlgorithmException complain) {
            SecurityException se = new SecurityException();
            se.initCause(complain);
            throw se;
        }
        return h;
    }
}
