    public static void query(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            Query query = entityManager.createQuery("SELECT p FROM Person p");

            Collection<Person> collection = (Collection<Person>) query.getResultList();

            for (Person person : collection) {

                System.out.println("found: " + person);

                for (Address address : person.getAddresses()) {

                    System.out.println("  with address: " + address);

                }

            }

        } finally {

            entityManager.close();

        }

    }
