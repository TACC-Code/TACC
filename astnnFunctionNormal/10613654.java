class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        if (jq.RunningNative) {
            System.err.println("Error: self-bootstrapping not supported (yet)");
            System.exit(-1);
        }
        String imageName = "jq.obj";
        String rootMethodClassName = "joeq.Main.JoeqVM";
        String rootMethodName = "boot";
        String classList = null;
        String addToClassList = null;
        boolean TrimAllTypes = false;
        boolean DUMP_COFF = false;
        boolean USE_BYTECODE_TRIMMER = true;
        jq.on_vm_startup = new LinkedList();
        CodeAddress.FACTORY = joeq.Bootstrap.BootstrapCodeAddress.FACTORY;
        HeapAddress.FACTORY = joeq.Bootstrap.BootstrapHeapAddress.FACTORY;
        jq.IsBootstrapping = true;
        ClassLibInterface.useJoeqClasslib(true);
        CodeAllocator.initializeCompiledMethodMap();
        HeapAllocator.initializeDataSegment();
        if (ClassLibInterface.DEFAULT.getClass().toString().indexOf("win32") != -1) {
            DUMP_COFF = true;
        } else {
            DUMP_COFF = false;
        }
        String osarch = System.getProperty("os.arch");
        if (osarch.equals("i386") || osarch.equals("x86")) {
            try {
                Class.forName("joeq.Scheduler.jq_x86RegisterState");
            } catch (ClassNotFoundException e) {
                System.err.println("Error: cannot load x86 module");
                System.exit(-1);
            }
            String default_compiler_name = System.getProperty("joeq.compiler", "joeq.Compiler.Reference.x86.x86ReferenceCompiler$Factory");
            Delegates.setDefaultCompiler(default_compiler_name);
        } else {
            System.err.println("Error: architecture " + osarch + " is not yet supported.");
            System.exit(-1);
        }
        String classpath = System.getProperty("sun.boot.class.path") + System.getProperty("path.separator") + System.getProperty("java.class.path");
        for (int i = 0; i < args.length; ) {
            int j = TraceFlags.setTraceFlag(args, i);
            if (i != j) {
                i = j;
                continue;
            }
            if (args[i].equals("-o")) {
                imageName = args[++i];
                ++i;
                continue;
            }
            if (args[i].equals("-r")) {
                String s = args[++i];
                int dotloc = s.lastIndexOf('.');
                rootMethodName = s.substring(dotloc + 1);
                rootMethodClassName = s.substring(0, dotloc);
                ++i;
                continue;
            }
            if (args[i].equals("-cp") || args[i].equals("-classpath")) {
                classpath = args[++i];
                ++i;
                continue;
            }
            if (args[i].equals("-cl") || args[i].equals("-classlist")) {
                classList = args[++i];
                ++i;
                continue;
            }
            if (args[i].equals("-a2cl") || args[i].equals("-addtoclasslist")) {
                addToClassList = args[++i];
                ++i;
                continue;
            }
            if (args[i].equals("-t")) {
                TrimAllTypes = true;
                ++i;
                continue;
            }
            if (args[i].equalsIgnoreCase("-borland")) {
                SinglePassBootImage.USE_MICROSOFT_STYLE_MUNGE = false;
                ++i;
                continue;
            }
            if (args[i].equalsIgnoreCase("-microsoft")) {
                SinglePassBootImage.USE_MICROSOFT_STYLE_MUNGE = true;
                ++i;
                continue;
            }
            err("unknown command line argument: " + args[i]);
        }
        rootMethodClassName = rootMethodClassName.replace('.', '/');
        System.out.println("Bootstrapping into " + imageName + ", " + (DUMP_COFF ? "COFF" : "ELF") + " format, root method " + rootMethodClassName + "." + rootMethodName + (TrimAllTypes ? ", trimming all types." : "."));
        for (Iterator it = PrimordialClassLoader.classpaths(classpath); it.hasNext(); ) {
            String s = (String) it.next();
            PrimordialClassLoader.loader.addToClasspath(s);
        }
        BootstrapCodeAllocator bca = BootstrapCodeAllocator.DEFAULT;
        DefaultCodeAllocator.default_allocator = bca;
        CodeAddress.FACTORY = BootstrapCodeAddress.FACTORY = new BootstrapCodeAddressFactory(bca);
        bca.init();
        ObjectTraverser obj_trav = ClassLibInterface.DEFAULT.getObjectTraverser();
        Reflection.obj_trav = obj_trav;
        obj_trav.initialize();
        objmap = SinglePassBootImage.DEFAULT;
        long starttime = System.currentTimeMillis();
        jq_Class c;
        c = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("L" + rootMethodClassName + ";");
        c.prepare();
        long loadtime = System.currentTimeMillis() - starttime;
        jq_StaticMethod rootm = null;
        Utf8 rootm_name = Utf8.get(rootMethodName);
        for (Iterator it = Arrays.asList(c.getDeclaredStaticMethods()).iterator(); it.hasNext(); ) {
            jq_StaticMethod m = (jq_StaticMethod) it.next();
            if (m.getName() == rootm_name) {
                rootm = m;
                break;
            }
        }
        if (rootm == null) err("root method not found: " + rootMethodClassName + "." + rootMethodName);
        Set classset = new HashSet();
        Set methodset;
        starttime = System.currentTimeMillis();
        if (addToClassList != null) {
            BufferedReader dis = new BufferedReader(new FileReader(addToClassList));
            for (; ; ) {
                String classname = dis.readLine();
                if (classname == null) break;
                if (classname.charAt(0) == '#') continue;
                jq_Type t = PrimordialClassLoader.loader.getOrCreateBSType(classname);
                t.prepare();
                classset.add(t);
            }
        }
        if (classList != null) {
            BufferedReader dis = new BufferedReader(new FileReader(classList));
            for (; ; ) {
                String classname = dis.readLine();
                if (classname == null) break;
                if (classname.equals("")) continue;
                if (classname.charAt(0) == '#') continue;
                if (classname.endsWith("*")) {
                    Assert._assert(classname.startsWith("L"));
                    Iterator i = PrimordialClassLoader.loader.listPackage(classname.substring(1, classname.length() - 1));
                    while (i.hasNext()) {
                        String s = (String) i.next();
                        Assert._assert(s.endsWith(".class"));
                        s = "L" + s.substring(0, s.length() - 6) + ";";
                        jq_Class t = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType(s);
                        t.prepare();
                        classset.add(t);
                        for (; ; ) {
                            jq_Array q = t.getArrayTypeForElementType();
                            q.prepare();
                            classset.add(q);
                            t = t.getSuperclass();
                            if (t == null) break;
                            classset.add(t);
                        }
                    }
                } else {
                    jq_Type t = PrimordialClassLoader.loader.getOrCreateBSType(classname);
                    t.prepare();
                    classset.add(t);
                    if (t instanceof jq_Class) {
                        jq_Class q = (jq_Class) t;
                        for (; ; ) {
                            t = q.getArrayTypeForElementType();
                            t.prepare();
                            classset.add(t);
                            q = q.getSuperclass();
                            if (q == null) break;
                            classset.add(q);
                        }
                    }
                }
            }
            methodset = new HashSet();
            Iterator i = classset.iterator();
            while (i.hasNext()) {
                jq_Type t = (jq_Type) i.next();
                if (t.isClassType()) {
                    jq_Class cl = (jq_Class) t;
                    jq_Method[] ms = cl.getDeclaredStaticMethods();
                    for (int k = 0; k < ms.length; ++k) {
                        methodset.add(ms[k]);
                    }
                    ms = cl.getDeclaredInstanceMethods();
                    for (int k = 0; k < ms.length; ++k) {
                        methodset.add(ms[k]);
                    }
                    ms = cl.getVirtualMethods();
                    for (int k = 0; k < ms.length; ++k) {
                        methodset.add(ms[k]);
                    }
                }
            }
        } else {
            if (USE_BYTECODE_TRIMMER) {
                Trimmer trim = new Trimmer(rootm, classset, !TrimAllTypes);
                trim.go();
                BootstrapRootSet rs = trim.getRootSet();
                System.out.println("Number of instantiated types: " + rs.getInstantiatedTypes().size());
                System.out.println("Number of necessary methods: " + rs.getNecessaryMethods().size());
                System.out.println("Number of necessary fields: " + rs.getNecessaryFields().size());
                classset = rs.getNecessaryTypes();
                System.out.println("Number of necessary classes: " + classset.size());
                if (TrimAllTypes) {
                    Iterator it = classset.iterator();
                    while (it.hasNext()) {
                        jq_Type t = (jq_Type) it.next();
                        System.out.println("Trimming type: " + t.getName());
                        Assert._assert(t.isPrepared());
                        if (t.isClassType()) {
                            rs.trimClass((jq_Class) t);
                        }
                    }
                    System.out.println("Number of instance fields kept: " + jq_Class.NumOfIFieldsKept);
                    System.out.println("Number of static fields kept: " + jq_Class.NumOfSFieldsKept);
                    System.out.println("Number of instance methods kept: " + jq_Class.NumOfIMethodsKept);
                    System.out.println("Number of static methods kept: " + jq_Class.NumOfSMethodsKept);
                    System.out.println("Number of instance fields eliminated: " + jq_Class.NumOfIFieldsEliminated);
                    System.out.println("Number of static fields eliminated: " + jq_Class.NumOfSFieldsEliminated);
                    System.out.println("Number of instance methods eliminated: " + jq_Class.NumOfIMethodsEliminated);
                    System.out.println("Number of static methods eliminated: " + jq_Class.NumOfSMethodsEliminated);
                }
                methodset = rs.getNecessaryMethods();
            } else {
                BootstrapRootSet rs = null;
                methodset = rs.getNecessaryMethods();
            }
        }
        loadtime += System.currentTimeMillis() - starttime;
        System.out.println("Load time: " + loadtime / 1000f + "s");
        if (classList == null) {
            dumpClassSet(classset);
            dumpMethodSet(methodset);
        }
        objmap.boot_types = classset;
        BootstrapCompilation comp = (BootstrapCompilation) CompilationState.DEFAULT;
        comp.setBootTypes(classset);
        if (false) {
            ArrayList class_list = new ArrayList(classset);
            Collections.sort(class_list, new Comparator() {

                public int compare(Object o1, Object o2) {
                    return ((jq_Type) o1).getDesc().toString().compareTo(((jq_Type) o2).getDesc().toString());
                }

                public boolean equals(Object o) {
                    return this == o;
                }
            });
            System.out.println("Types:");
            Set packages = new LinearSet();
            Iterator it = class_list.iterator();
            while (it.hasNext()) {
                jq_Type t = (jq_Type) it.next();
                String s = t.getDesc().toString();
                System.out.println(s);
                if (s.charAt(0) == 'L') {
                    int index = s.lastIndexOf('/');
                    if (index == -1) s = ""; else s = s.substring(1, index + 1);
                    packages.add(s);
                }
            }
            System.out.println("Packages:");
            it = packages.iterator();
            while (it.hasNext()) {
                System.out.println("L" + it.next() + "*");
            }
        }
        objmap.enableAllocations();
        starttime = System.currentTimeMillis();
        SystemInterface._class.sf_initialize();
        Iterator it = classset.iterator();
        while (it.hasNext()) {
            jq_Type t = (jq_Type) it.next();
            Assert._assert(t.isPrepared());
            t.sf_initialize();
        }
        long sfinittime = System.currentTimeMillis() - starttime;
        System.out.println("SF init time: " + sfinittime / 1000f + "s");
        starttime = System.currentTimeMillis();
        it = methodset.iterator();
        while (it.hasNext()) {
            jq_Member m = (jq_Member) it.next();
            if (m instanceof jq_Method) {
                jq_Method m2 = ((jq_Method) m);
                if (m2.getDeclaringClass() == Unsafe._class) continue;
                if (m2.getDeclaringClass().isAddressType()) continue;
                m2.compile();
            }
        }
        long compiletime = System.currentTimeMillis() - starttime;
        System.out.println("Compile time: " + compiletime / 1000f + "s");
        int numTypes = PrimordialClassLoader.loader.getNumTypes();
        jq_Type[] types = PrimordialClassLoader.loader.getAllTypes();
        for (int i = 0; i < numTypes; ++i) {
            jq_Type t = types[i];
            Reflection.getJDKType(t);
        }
        starttime = System.currentTimeMillis();
        it = classset.iterator();
        while (it.hasNext()) {
            jq_Type t = (jq_Type) it.next();
            Assert._assert(t.isSFInitialized());
            if (t.isClassType()) {
                jq_Class k = (jq_Class) t;
                if (k.getSuperclass() != null && !classset.contains(k.getSuperclass())) {
                    Assert.UNREACHABLE(k.getSuperclass() + " (superclass of " + k + ") is not in class set!");
                }
                jq_StaticField[] sfs = k.getDeclaredStaticFields();
                for (int j = 0; j < sfs.length; ++j) {
                    jq_StaticField sf = sfs[j];
                    if (sf.getType().isReferenceType() && !sf.getType().isAddressType()) {
                        Object val = Reflection.getstatic_A(sf);
                        objmap.getOrAllocateObject(val);
                    }
                }
            }
        }
        it = classset.iterator();
        while (it.hasNext()) {
            jq_Type t = (jq_Type) it.next();
            Assert._assert(t.isSFInitialized());
            if (t == Unsafe._class) continue;
            t.compile();
            t.cls_initialize();
            objmap.initializeObject(t);
        }
        objmap.reinitializeObjects();
        objmap.initializeObject(CodeAllocator.compiledMethods);
        System.out.println("Number of classes seen = " + PrimordialClassLoader.loader.getNumTypes());
        System.out.println("Number of classes in image = " + objmap.boot_types.size());
        objmap.handleForwardReferences();
        Utf8.NO_NEW = true;
        it = classset.iterator();
        while (it.hasNext()) {
            jq_Type t = (jq_Type) it.next();
            if (t.isReferenceType()) {
                jq_Reference r = (jq_Reference) t;
                if (r != Unsafe._class) {
                    objmap.initVTable(r);
                }
                if (r.isClassType()) {
                    jq_Class k = (jq_Class) t;
                    objmap.initStaticFields(k);
                }
            }
        }
        jq_Class jq_class = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljoeq/Main/jq;");
        Assert._assert(classset.contains(jq_class));
        jq_class.setStaticData(jq_class.getOrCreateStaticField("RunningNative", "Z"), 1);
        jq_class.setStaticData(jq_class.getOrCreateStaticField("IsBootstrapping", "Z"), 0);
        jq_Class utf8_class = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljoeq/UTF/Utf8;");
        utf8_class.setStaticData(utf8_class.getOrCreateStaticField("NO_NEW", "Z"), 0);
        HeapAddress addr = HeapAddress.addressOf(jq.on_vm_startup);
        jq_StaticField _on_vm_startup = jq_class.getOrCreateStaticField("on_vm_startup", "Ljava/util/List;");
        jq_class.setStaticData(_on_vm_startup, addr);
        objmap.addDataReloc(_on_vm_startup.getAddress(), addr);
        it = classset.iterator();
        while (it.hasNext()) {
            jq_Type t = (jq_Type) it.next();
            if (t.isClassType()) {
                jq_Class k = (jq_Class) t;
                objmap.initStaticData(k);
                objmap.addStaticFieldRelocs(k);
            }
        }
        objmap.reinitializeObjects();
        objmap.handleForwardReferences();
        long traversaltime = System.currentTimeMillis() - starttime;
        objmap.disableAllocations();
        System.out.println("Scanned: " + objmap.numOfEntries() + " objects, memory used: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "                    ");
        System.out.println("Scan time: " + traversaltime / 1000f + "s");
        System.out.println("Total number of Utf8 = " + (Utf8.size + 1));
        System.out.println("Code segment size = " + bca.size());
        System.out.println("Data segment size = " + objmap.size());
        objmap.initStaticField(CodeAllocator._lowAddress);
        objmap.initStaticField(CodeAllocator._highAddress);
        objmap.initStaticData(CodeAllocator._class);
        HeapAllocator.data_segment_start = new BootstrapHeapAddress(-1);
        HeapAllocator.data_segment_end = new BootstrapHeapAddress(objmap.size());
        objmap.initStaticField(HeapAllocator._data_segment_start);
        objmap.initStaticField(HeapAllocator._data_segment_end);
        objmap.initStaticData(HeapAllocator._class);
        File f = new File(imageName);
        if (f.exists()) f.delete();
        RandomAccessFile fos = new RandomAccessFile(imageName, "rw");
        FileChannel fc = fos.getChannel();
        starttime = System.currentTimeMillis();
        try {
            if (DUMP_COFF) objmap.dumpCOFF(fc, rootm); else objmap.dumpELF(fc, rootm);
        } finally {
            fc.close();
            fos.close();
        }
        long dumptime = System.currentTimeMillis() - starttime;
        System.out.println("Dump time: " + dumptime / 1000f + "s");
        System.out.println(rootm.getDefaultCompiledVersion());
    }
}
