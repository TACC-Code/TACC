class BackupThread extends Thread {
    private ResultCode generateSourceFile(final Schema schema, final boolean terse) {
        final TreeMap<String, AttributeTypeDefinition> requiredAttrs = new TreeMap<String, AttributeTypeDefinition>();
        final TreeMap<String, AttributeTypeDefinition> optionalAttrs = new TreeMap<String, AttributeTypeDefinition>();
        final TreeMap<String, TreeSet<String>> requiredAttrOCs = new TreeMap<String, TreeSet<String>>();
        final TreeMap<String, TreeSet<String>> optionalAttrOCs = new TreeMap<String, TreeSet<String>>();
        final TreeMap<String, String> types = new TreeMap<String, String>();
        final String structuralClassName = structuralClassArg.getValue();
        final ObjectClassDefinition structuralOC = schema.getObjectClass(structuralClassName);
        if (structuralOC == null) {
            err(ERR_GEN_SOURCE_STRUCTURAL_CLASS_NOT_FOUND.get(structuralClassName));
            return ResultCode.PARAM_ERROR;
        }
        if (structuralOC.getObjectClassType(schema) != ObjectClassType.STRUCTURAL) {
            err(ERR_GEN_SOURCE_STRUCTURAL_CLASS_NOT_STRUCTURAL.get(structuralClassName));
            return ResultCode.PARAM_ERROR;
        }
        processObjectClass(structuralOC, schema, requiredAttrs, requiredAttrOCs, optionalAttrs, optionalAttrOCs, types);
        final TreeMap<String, ObjectClassDefinition> auxiliaryOCs = new TreeMap<String, ObjectClassDefinition>();
        if (auxiliaryClassArg.isPresent()) {
            for (final String s : auxiliaryClassArg.getValues()) {
                final ObjectClassDefinition oc = schema.getObjectClass(s);
                if (oc == null) {
                    err(ERR_GEN_SOURCE_AUXILIARY_CLASS_NOT_FOUND.get(s));
                    return ResultCode.PARAM_ERROR;
                }
                if (oc.getObjectClassType(schema) != ObjectClassType.AUXILIARY) {
                    err(ERR_GEN_SOURCE_AUXILIARY_CLASS_NOT_AUXILIARY.get(s));
                    return ResultCode.PARAM_ERROR;
                }
                auxiliaryOCs.put(toLowerCase(s), oc);
                processObjectClass(oc, schema, requiredAttrs, requiredAttrOCs, optionalAttrs, optionalAttrOCs, types);
            }
        }
        final TreeMap<String, ObjectClassDefinition> superiorOCs = new TreeMap<String, ObjectClassDefinition>();
        for (final ObjectClassDefinition s : structuralOC.getSuperiorClasses(schema, true)) {
            superiorOCs.put(toLowerCase(s.getNameOrOID()), s);
        }
        for (final ObjectClassDefinition d : auxiliaryOCs.values()) {
            for (final ObjectClassDefinition s : d.getSuperiorClasses(schema, true)) {
                superiorOCs.put(toLowerCase(s.getNameOrOID()), s);
            }
        }
        superiorOCs.remove(toLowerCase(structuralClassName));
        for (final String s : auxiliaryOCs.keySet()) {
            superiorOCs.remove(s);
        }
        final TreeMap<String, AttributeTypeDefinition> operationalAttrs = new TreeMap<String, AttributeTypeDefinition>();
        if (operationalAttributeArg.isPresent()) {
            for (final String s : operationalAttributeArg.getValues()) {
                final AttributeTypeDefinition d = schema.getAttributeType(s);
                if (d == null) {
                    err(ERR_GEN_SOURCE_OPERATIONAL_ATTRIBUTE_NOT_DEFINED.get(s));
                    return ResultCode.PARAM_ERROR;
                } else if (!d.isOperational()) {
                    err(ERR_GEN_SOURCE_OPERATIONAL_ATTRIBUTE_NOT_OPERATIONAL.get(s));
                    return ResultCode.PARAM_ERROR;
                } else {
                    final String lowerName = toLowerCase(s);
                    operationalAttrs.put(lowerName, d);
                    types.put(lowerName, getJavaType(schema, d));
                }
            }
        }
        final TreeSet<String> rdnAttrs = new TreeSet<String>();
        for (final String s : rdnAttributeArg.getValues()) {
            final AttributeTypeDefinition d = schema.getAttributeType(s);
            if (d == null) {
                err(ERR_GEN_SOURCE_RDN_ATTRIBUTE_NOT_DEFINED.get(s));
                return ResultCode.PARAM_ERROR;
            }
            final String lowerName = toLowerCase(d.getNameOrOID());
            rdnAttrs.add(lowerName);
            if (requiredAttrs.containsKey(lowerName)) {
            } else if (optionalAttrs.containsKey(lowerName)) {
                requiredAttrs.put(lowerName, optionalAttrs.remove(lowerName));
                requiredAttrOCs.put(lowerName, optionalAttrOCs.remove(lowerName));
            } else {
                err(ERR_GEN_SOURCE_RDN_ATTRIBUTE_NOT_DEFINED.get(s));
                return ResultCode.PARAM_ERROR;
            }
        }
        final TreeSet<String> lazyAttrs = new TreeSet<String>();
        for (final String s : lazyAttributeArg.getValues()) {
            final AttributeTypeDefinition d = schema.getAttributeType(s);
            if (d == null) {
                err(ERR_GEN_SOURCE_LAZY_ATTRIBUTE_NOT_DEFINED.get(s));
                return ResultCode.PARAM_ERROR;
            }
            final String lowerName = toLowerCase(d.getNameOrOID());
            lazyAttrs.add(lowerName);
            if (requiredAttrs.containsKey(lowerName) || optionalAttrs.containsKey(lowerName) || operationalAttrs.containsKey(lowerName)) {
            } else {
                err(ERR_GEN_SOURCE_LAZY_ATTRIBUTE_NOT_ALLOWED.get(s));
                return ResultCode.PARAM_ERROR;
            }
        }
        final String className;
        if (classNameArg.isPresent()) {
            className = classNameArg.getValue();
            final StringBuilder invalidReason = new StringBuilder();
            if (!PersistUtils.isValidJavaIdentifier(className, invalidReason)) {
                err(ERR_GEN_SOURCE_INVALID_CLASS_NAME.get(className, invalidReason.toString()));
                return ResultCode.PARAM_ERROR;
            }
        } else {
            className = capitalize(PersistUtils.toJavaIdentifier(structuralClassName));
        }
        final File sourceFile = new File(outputDirectoryArg.getValue(), className + ".java");
        final PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(sourceFile));
        } catch (Exception e) {
            debugException(e);
            err(ERR_GEN_SOURCE_CANNOT_CREATE_WRITER.get(sourceFile.getAbsolutePath(), getExceptionMessage(e)));
            return ResultCode.LOCAL_ERROR;
        }
        if (packageNameArg.isPresent()) {
            final String packageName = packageNameArg.getValue();
            if (packageName.length() > 0) {
                writer.println("package " + packageName + ';');
                writer.println();
                writer.println();
                writer.println();
            }
        }
        boolean javaImports = false;
        if (needArrays) {
            writer.println("import " + Arrays.class.getName() + ';');
            javaImports = true;
        }
        if (needDate) {
            writer.println("import " + Date.class.getName() + ';');
            javaImports = true;
        }
        if (javaImports) {
            writer.println();
        }
        if (needDN) {
            writer.println("import " + DN.class.getName() + ';');
        }
        writer.println("import " + Entry.class.getName() + ';');
        writer.println("import " + Filter.class.getName() + ';');
        if (needDN) {
            writer.println("import " + LDAPException.class.getName() + ';');
            writer.println("import " + LDAPInterface.class.getName() + ';');
        }
        writer.println("import " + ReadOnlyEntry.class.getName() + ';');
        writer.println("import " + DefaultObjectEncoder.class.getName() + ';');
        writer.println("import " + FieldInfo.class.getName() + ';');
        writer.println("import " + FilterUsage.class.getName() + ';');
        writer.println("import " + LDAPEntryField.class.getName() + ';');
        writer.println("import " + LDAPField.class.getName() + ';');
        writer.println("import " + LDAPObject.class.getName() + ';');
        writer.println("import " + LDAPObjectHandler.class.getName() + ';');
        writer.println("import " + LDAPPersister.class.getName() + ';');
        writer.println("import " + LDAPPersistException.class.getName() + ';');
        if (needPersistedObjects) {
            writer.println("import " + PersistedObjects.class.getName() + ';');
        }
        writer.println("import " + PersistFilterType.class.getName() + ';');
        if (needDN) {
            writer.println("import " + PersistUtils.class.getName() + ';');
        }
        writer.println();
        writer.println();
        writer.println();
        writer.println("/**");
        writer.println(" * This class provides an implementation of an object " + "that can be used to");
        writer.println(" * represent " + structuralClassName + " objects in the directory.");
        writer.println(" * It was generated by the " + getToolName() + " tool provided with the");
        writer.println(" * UnboundID LDAP SDK for Java.  It " + "may be customized as desired to better suit");
        writer.println(" * your needs.");
        writer.println(" */");
        writer.println("@LDAPObject(structuralClass=\"" + structuralClassName + "\",");
        switch(auxiliaryOCs.size()) {
            case 0:
                break;
            case 1:
                writer.println("            auxiliaryClass=\"" + auxiliaryOCs.values().iterator().next().getNameOrOID() + "\",");
                break;
            default:
                final Iterator<ObjectClassDefinition> iterator = auxiliaryOCs.values().iterator();
                writer.println("            auxiliaryClass={ \"" + iterator.next().getNameOrOID() + "\",");
                while (iterator.hasNext()) {
                    final String ocName = iterator.next().getNameOrOID();
                    if (iterator.hasNext()) {
                        writer.println("                             \"" + ocName + "\",");
                    } else {
                        writer.println("                             \"" + ocName + "\" },");
                    }
                }
                break;
        }
        switch(superiorOCs.size()) {
            case 0:
                break;
            case 1:
                writer.println("            superiorClass=\"" + superiorOCs.values().iterator().next().getNameOrOID() + "\",");
                break;
            default:
                final Iterator<ObjectClassDefinition> iterator = superiorOCs.values().iterator();
                writer.println("            superiorClass={ \"" + iterator.next().getNameOrOID() + "\",");
                while (iterator.hasNext()) {
                    final String ocName = iterator.next().getNameOrOID();
                    if (iterator.hasNext()) {
                        writer.println("                             \"" + ocName + "\",");
                    } else {
                        writer.println("                             \"" + ocName + "\" },");
                    }
                }
                break;
        }
        if (defaultParentDNArg.isPresent()) {
            writer.println("            defaultParentDN=\"" + defaultParentDNArg.getValue() + "\",");
        }
        writer.println("            postDecodeMethod=\"doPostDecode\",");
        writer.println("            postEncodeMethod=\"doPostEncode\")");
        writer.println("public class " + className);
        writer.println("{");
        if (!terse) {
            writer.println("  /*");
            writer.println("   * NOTE:  This class includes a number of annotation " + "elements which are not");
            writer.println("   * required but have been provided to make it easier " + "to edit the resulting");
            writer.println("   * source code.  If you want to exclude these " + "unnecessary annotation");
            writer.println("   * elements, use the '--terse' command-line argument.");
            writer.println("   */");
            writer.println();
            writer.println();
            writer.println();
        }
        writer.println("  // The field to use to hold a read-only copy of the " + "associated entry.");
        writer.println("  @LDAPEntryField()");
        writer.println("  private ReadOnlyEntry ldapEntry;");
        for (final String lowerName : rdnAttrs) {
            final AttributeTypeDefinition d = requiredAttrs.get(lowerName);
            final TreeSet<String> ocNames = requiredAttrOCs.get(lowerName);
            writeField(writer, d, types.get(lowerName), ocNames, true, true, structuralClassName, false, terse);
        }
        for (final String lowerName : requiredAttrs.keySet()) {
            if (rdnAttrs.contains(lowerName)) {
                continue;
            }
            final AttributeTypeDefinition d = requiredAttrs.get(lowerName);
            final TreeSet<String> ocNames = requiredAttrOCs.get(lowerName);
            writeField(writer, d, types.get(lowerName), ocNames, false, true, structuralClassName, lazyAttrs.contains(lowerName), terse);
        }
        for (final String lowerName : optionalAttrs.keySet()) {
            final AttributeTypeDefinition d = optionalAttrs.get(lowerName);
            final TreeSet<String> ocNames = optionalAttrOCs.get(lowerName);
            writeField(writer, d, types.get(lowerName), ocNames, false, false, structuralClassName, lazyAttrs.contains(lowerName), terse);
        }
        for (final String lowerName : operationalAttrs.keySet()) {
            final AttributeTypeDefinition d = operationalAttrs.get(lowerName);
            final TreeSet<String> ocNames = EMPTY_TREE_SET;
            writeField(writer, d, types.get(lowerName), ocNames, false, false, structuralClassName, lazyAttrs.contains(lowerName), terse);
        }
        writer.println();
        writer.println();
        writer.println();
        writer.println("  /**");
        writer.println("   * Creates a new instance of this object.  All fields " + "will be uninitialized,");
        writer.println("   * so the setter methods should be used to assign " + "values to them.");
        writer.println("   */");
        writer.println("  public " + className + "()");
        writer.println("  {");
        writer.println("    // No initialization will be performed by default.  " + "Note that if you set");
        writer.println("    // values for any fields marked with an @LDAPField, " + "@LDAPDNField, or");
        writer.println("    // @LDAPEntryField annotation, they will be " + "overwritten in the course of");
        writer.println("    // decoding initializing this object from an LDAP " + "entry.");
        writer.println("  }");
        writer.println();
        writer.println();
        writer.println();
        writer.println("  /**");
        writer.println("   * Creates a new " + className + " object decoded");
        writer.println("   * from the provided entry.");
        writer.println("   *");
        writer.println("   * @param  entry  The entry to be decoded.");
        writer.println("   *");
        writer.println("   * @return  The decoded " + className + " object.");
        writer.println("   *");
        writer.println("   * @throws  LDAPPersistException  If a problem occurs " + "while attempting to");
        writer.println("   *                                decode the provided " + "entry.");
        writer.println("   */");
        writer.println("  public static " + className + " decode(final Entry entry)");
        writer.println("         throws LDAPPersistException");
        writer.println("  {");
        writer.println("    return getPersister().decode(entry);");
        writer.println("  }");
        writer.println("");
        writer.println("");
        writer.println("");
        writer.println("  /**");
        writer.println("   * Retrieves an {@code LDAPPersister} instance that " + "may be used to interact");
        writer.println("   * with objects of this type.");
        writer.println("   *");
        writer.println("   * @return  An {@code LDAPPersister} instance that may " + "be used to interact");
        writer.println("   *          with objects of this type.");
        writer.println("   *");
        writer.println("   * @throws  LDAPPersistException  If a problem occurs " + "while creating the");
        writer.println("   *                                " + "{@code LDAPPersister} instance.");
        writer.println("   */");
        writer.println("  public static LDAPPersister<" + className + "> getPersister()");
        writer.println("         throws LDAPPersistException");
        writer.println("  {");
        writer.println("    return LDAPPersister.getInstance(" + className + ".class);");
        writer.println("  }");
        writer.println();
        writer.println();
        writer.println();
        writer.println("  /**");
        writer.println("   * Performs any processing that may be necessary after " + "initializing this");
        writer.println("   * object from an LDAP entry.");
        writer.println("   *");
        writer.println("   * @throws  LDAPPersistException  If the generated " + "entry should not be used.");
        writer.println("   */");
        writer.println("  private void doPostDecode()");
        writer.println("          throws LDAPPersistException");
        writer.println("  {");
        writer.println("    // No processing is needed by default.  You may " + "provide an implementation");
        writer.println("    // for this method if custom post-decode processing " + "is needed.");
        writer.println("  }");
        writer.println();
        writer.println();
        writer.println();
        writer.println("  /**");
        writer.println("   * Performs any processing that may be necessary after " + "encoding this object");
        writer.println("   * to an LDAP entry.");
        writer.println("   *");
        writer.println("   * @param  entry  The entry that has been generated.  " + "It may be altered if");
        writer.println("   *                desired.");
        writer.println("   *");
        writer.println("   * @throws  LDAPPersistException  If there is a " + "problem with the object after");
        writer.println("   *                                it has been decoded " + "from an LDAP entry.");
        writer.println("   */");
        writer.println("  private void doPostEncode(final Entry entry)");
        writer.println("          throws LDAPPersistException");
        writer.println("  {");
        writer.println("    // No processing is needed by default.  You may " + "provide an implementation");
        writer.println("    // for this method if custom post-encode processing " + "is needed.");
        writer.println("  }");
        writer.println();
        writer.println();
        writer.println();
        writer.println("  /**");
        writer.println("   * Retrieves a read-only copy of the entry with which " + "this object is");
        writer.println("   * associated, if it is available.  It will only be " + "available if this object");
        writer.println("   * was decoded from or encoded to an LDAP entry.");
        writer.println("   *");
        writer.println("   * @return  A read-only copy of the entry with which " + "this object is");
        writer.println("   *          associated, or {@code null} if it is not " + "available.");
        writer.println("   */");
        writer.println("  public ReadOnlyEntry getLDAPEntry()");
        writer.println("  {");
        writer.println("    return ldapEntry;");
        writer.println("  }");
        writer.println();
        writer.println();
        writer.println();
        writer.println("  /**");
        writer.println("   * Retrieves the DN of the entry with which this " + "object is associated, if it");
        writer.println("   * is available.  It will only be available if this " + "object was decoded from or");
        writer.println("   * encoded to an LDAP entry.");
        writer.println("   *");
        writer.println("   * @return  The DN of the entry with which this object " + "is associated, or");
        writer.println("   *          {@code null} if it is not available.");
        writer.println("   */");
        writer.println("  public String getLDAPEntryDN()");
        writer.println("  {");
        writer.println("    if (ldapEntry == null)");
        writer.println("    {");
        writer.println("      return null;");
        writer.println("    }");
        writer.println("    else");
        writer.println("    {");
        writer.println("      return ldapEntry.getDN();");
        writer.println("    }");
        writer.println("  }");
        for (final String lowerName : rdnAttrs) {
            final AttributeTypeDefinition d = requiredAttrs.get(lowerName);
            writeFieldMethods(writer, d, types.get(lowerName), true);
        }
        for (final String lowerName : requiredAttrs.keySet()) {
            if (rdnAttrs.contains(lowerName)) {
                continue;
            }
            final AttributeTypeDefinition d = requiredAttrs.get(lowerName);
            writeFieldMethods(writer, d, types.get(lowerName), true);
        }
        for (final String lowerName : optionalAttrs.keySet()) {
            final AttributeTypeDefinition d = optionalAttrs.get(lowerName);
            writeFieldMethods(writer, d, types.get(lowerName), true);
        }
        for (final String lowerName : operationalAttrs.keySet()) {
            final AttributeTypeDefinition d = operationalAttrs.get(lowerName);
            writeFieldMethods(writer, d, types.get(lowerName), false);
        }
        writeToString(writer, className, requiredAttrs.values(), optionalAttrs.values(), operationalAttrs.values());
        writer.println("}");
        writer.println();
        writer.close();
        return ResultCode.SUCCESS;
    }
}
