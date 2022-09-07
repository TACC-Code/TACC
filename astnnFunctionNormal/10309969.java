class BackupThread extends Thread {
    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    String nom = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    long delai = org.fudaa.dodico.corba.base.TempsHelper.read(in);
                    org.omg.CORBA.Object $result = null;
                    $result = this.chercheServeurParNom(nom, delai);
                    out = $rh.createReply();
                    org.omg.CORBA.ObjectHelper.write(out, $result);
                    break;
                }
            case 1:
                {
                    String face = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    long delai = org.fudaa.dodico.corba.base.TempsHelper.read(in);
                    org.omg.CORBA.Object $result = null;
                    $result = this.chercheServeurParInterface(face, delai);
                    out = $rh.createReply();
                    org.omg.CORBA.ObjectHelper.write(out, $result);
                    break;
                }
            case 2:
                {
                    String $result[] = null;
                    $result = this.nomsServeur();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 3:
                {
                    String face = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    String $result[] = null;
                    $result = this.nomsServeurParInterface(face);
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 4:
                {
                    int $result = (int) 0;
                    $result = this.creation();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 5:
                {
                    int $result = (int) 0;
                    $result = this.derniereUtilisation();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 6:
                {
                    String $result = null;
                    $result = this.description();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 7:
                {
                    org.fudaa.dodico.corba.objet.IPersonne $result = null;
                    $result = this.responsable();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IPersonneHelper.write(out, $result);
                    break;
                }
            case 8:
                {
                    org.fudaa.dodico.corba.objet.IConnexion $result[] = null;
                    $result = this.connexions();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.VIConnexionHelper.write(out, $result);
                    break;
                }
            case 9:
                {
                    org.fudaa.dodico.corba.objet.IPersonne p = org.fudaa.dodico.corba.objet.IPersonneHelper.read(in);
                    org.fudaa.dodico.corba.objet.IConnexion $result = null;
                    $result = this.connexion(p);
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IConnexionHelper.write(out, $result);
                    break;
                }
            case 10:
                {
                    org.fudaa.dodico.corba.objet.IConnexion c = org.fudaa.dodico.corba.objet.IConnexionHelper.read(in);
                    boolean $result = false;
                    $result = this.deconnexion(c);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 11:
                {
                    this.dispose();
                    out = $rh.createReply();
                    break;
                }
            case 12:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    this.initialise(o);
                    out = $rh.createReply();
                    break;
                }
            case 13:
                {
                    String nom = org.fudaa.dodico.corba.base.ChaineHelper.read(in);
                    this.reconnecte(nom);
                    out = $rh.createReply();
                    break;
                }
            case 14:
                {
                    org.fudaa.dodico.corba.objet.IObjet $result = null;
                    $result = this.creeClone();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.objet.IObjetHelper.write(out, $result);
                    break;
                }
            case 15:
                {
                    String $result = null;
                    $result = this.moduleCorba();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 16:
                {
                    String $result[] = null;
                    $result = this.interfacesCorba();
                    out = $rh.createReply();
                    org.fudaa.dodico.corba.base.VChaineHelper.write(out, $result);
                    break;
                }
            case 17:
                {
                    org.fudaa.dodico.corba.objet.IObjet o = org.fudaa.dodico.corba.objet.IObjetHelper.read(in);
                    boolean $result = false;
                    $result = this.egale(o);
                    out = $rh.createReply();
                    out.write_boolean($result);
                    break;
                }
            case 18:
                {
                    int $result = (int) 0;
                    $result = this.codeHachage();
                    out = $rh.createReply();
                    out.write_long($result);
                    break;
                }
            case 19:
                {
                    String $result = null;
                    $result = this.enChaine();
                    out = $rh.createReply();
                    out.write_string($result);
                    break;
                }
            case 20:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        org.omg.CORBA.Object obj = org.omg.CORBA.ObjectHelper.read(in);
                        this.bind(n, obj);
                        out = $rh.createReply();
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.AlreadyBound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper.write(out, $ex);
                    }
                    break;
                }
            case 21:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        org.omg.CORBA.Object obj = org.omg.CORBA.ObjectHelper.read(in);
                        this.rebind(n, obj);
                        out = $rh.createReply();
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    }
                    break;
                }
            case 22:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        org.omg.CosNaming.NamingContext nc = org.omg.CosNaming.NamingContextHelper.read(in);
                        this.bind_context(n, nc);
                        out = $rh.createReply();
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.AlreadyBound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper.write(out, $ex);
                    }
                    break;
                }
            case 23:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        org.omg.CosNaming.NamingContext nc = org.omg.CosNaming.NamingContextHelper.read(in);
                        this.rebind_context(n, nc);
                        out = $rh.createReply();
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    }
                    break;
                }
            case 24:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        org.omg.CORBA.Object $result = null;
                        $result = this.resolve(n);
                        out = $rh.createReply();
                        org.omg.CORBA.ObjectHelper.write(out, $result);
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    }
                    break;
                }
            case 25:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        this.unbind(n);
                        out = $rh.createReply();
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    }
                    break;
                }
            case 26:
                {
                    org.omg.CosNaming.NamingContext $result = null;
                    $result = this.new_context();
                    out = $rh.createReply();
                    org.omg.CosNaming.NamingContextHelper.write(out, $result);
                    break;
                }
            case 27:
                {
                    try {
                        org.omg.CosNaming.NameComponent n[] = org.omg.CosNaming.NameHelper.read(in);
                        org.omg.CosNaming.NamingContext $result = null;
                        $result = this.bind_new_context(n);
                        out = $rh.createReply();
                        org.omg.CosNaming.NamingContextHelper.write(out, $result);
                    } catch (org.omg.CosNaming.NamingContextPackage.NotFound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.AlreadyBound $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(out, $ex);
                    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.InvalidNameHelper.write(out, $ex);
                    }
                    break;
                }
            case 28:
                {
                    try {
                        this.destroy();
                        out = $rh.createReply();
                    } catch (org.omg.CosNaming.NamingContextPackage.NotEmpty $ex) {
                        out = $rh.createExceptionReply();
                        org.omg.CosNaming.NamingContextPackage.NotEmptyHelper.write(out, $ex);
                    }
                    break;
                }
            case 29:
                {
                    int how_many = in.read_ulong();
                    org.omg.CosNaming.BindingListHolder bl = new org.omg.CosNaming.BindingListHolder();
                    org.omg.CosNaming.BindingIteratorHolder bi = new org.omg.CosNaming.BindingIteratorHolder();
                    this.list(how_many, bl, bi);
                    out = $rh.createReply();
                    org.omg.CosNaming.BindingListHelper.write(out, bl.value);
                    org.omg.CosNaming.BindingIteratorHelper.write(out, bi.value);
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }
}
