        Constructor declaredCtors[] = c.getDeclaredConstructors();

        Arrays.sort(declaredCtors, new Comparator() {



            public int compare(Object x, Object y) {

                return x.toString().compareTo(y.toString());

            }

        });

        System.out.println(c + " has " + declaredCtors.length + " declared constructors");

        for (int i = 0; i < declaredCtors.length; ++i) System.out.println("   " + i + ": " + declaredCtors[i]);

        Method methods[] = c.getMethods();
