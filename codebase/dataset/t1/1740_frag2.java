    public static void query(EntityManagerFactory entityManagerFactory) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            Query query = entityManager.createQuery("SELECT p FROM Person p");

            Collection<Person> collection = (Collection<Person>) query.getResultList();

            for (Person person : collection) {

                LOGGER.info("found: " + person);

                LOGGER.info("  with address: " + person.getAddress());

            }

        } finally {

            entityManager.close();

        }

    }
