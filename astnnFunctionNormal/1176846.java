class BackupThread extends Thread {
    protected PropertyDescriptor createPropertyDescriptor(Field field) {
        PropertyDescriptor descriptor = null;
        if (field != null) {
            if (this.shouldGenerate(field)) {
                BeanProperty property = this.getBeanPropertyAnnotation(field);
                if (property != null && !(!this.considerExpertDescriptor() && property.expert())) {
                    String name = property.name();
                    if (name != null) {
                        boolean generate = true;
                        if (this.getBeanInfoCategory().equals(BeanInfoCategory.BASICS) && this.isAllowedPropertiesMecanismActivated()) {
                            generate = false;
                            if (this.getAllowedPropertiesByName() != null) {
                                for (int i = 0; i < this.getAllowedPropertiesByName().length; i++) {
                                    String current = this.getAllowedPropertiesByName()[i];
                                    if (name.equals(current)) {
                                        generate = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (generate) {
                            try {
                                Method readMethod = this.getReadMethod(field, property);
                                Method writeMethod = this.getWriteMethod(field, property);
                                descriptor = new PropertyDescriptor(name, readMethod, writeMethod);
                                this.preProcessProperty(descriptor);
                                descriptor.setName(property.name());
                                descriptor.setDisplayName(TypeInformationProvider.getPropertyDisplayName(this.getRelatedClass(), field.getName()));
                                descriptor.setExpert(property.expert());
                                descriptor.setHidden(property.hidden());
                                descriptor.setPreferred(property.preferred());
                                descriptor.setShortDescription(TypeInformationProvider.getPropertyShortDescription(this.getRelatedClass(), field.getName()));
                                descriptor.setBound(property.bound());
                                descriptor.setConstrained(property.constrained());
                                this.postProcessProperty(descriptor);
                            } catch (IntrospectionException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                    }
                }
            }
        }
        return descriptor;
    }
}
