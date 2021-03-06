    protected void nullInstance(Object instance) {

        if (instance == null) {

            return;

        }

        Field[] fields = instance.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];

            int mods = field.getModifiers();

            if (field.getType().isPrimitive() || (field.getName().indexOf("$") != -1)) {

                continue;

            }

            try {

                field.setAccessible(true);

                if (Modifier.isStatic(mods) && Modifier.isFinal(mods)) {

                    continue;

                } else {

                    Object value = field.get(instance);

                    if (null != value) {

                        Class valueClass = value.getClass();

                        if (!loadedByThisOrChild(valueClass)) {

                            if (log.isDebugEnabled()) {

                                log.debug("Not setting field " + field.getName() + " to null in object of class " + instance.getClass().getName() + " because the referenced object was of type " + valueClass.getName() + " which was not loaded by this WebappClassLoader.");

                            }

                        } else {

                            field.set(instance, null);

                            if (log.isDebugEnabled()) {

                                log.debug("Set field " + field.getName() + " to null in class " + instance.getClass().getName());

                            }

                        }

                    }

                }

            } catch (Throwable t) {

                if (log.isDebugEnabled()) {

                    log.debug("Could not set field " + field.getName() + " to null in object instance of class " + instance.getClass().getName(), t);

                }

            }

        }

    }
