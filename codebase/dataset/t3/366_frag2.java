    InputStream getResourceAsStream(final ClassLoader cl, final String name) {

        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {



            public Object run() {

                InputStream ris;

                if (cl == null) {

                    ris = ClassLoader.getSystemResourceAsStream(name);

                } else {

                    ris = cl.getResourceAsStream(name);

                }

                return ris;

            }

        });

    }
