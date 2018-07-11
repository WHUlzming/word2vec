    private static void insert(SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();

        try {

            session.beginTransaction();

            Person person1 = new Person();

            person1.setFirstName("Jesse");

            person1.setLastName("James");

            person1.setHomeState(State.CT);

            person1.setIncome(IncomeGroup.average);

            Person person2 = new Person();

            person2.setFirstName("James");

            person2.setLastName("Bloch");

            person2.setHomeState(State.AZ);

            person2.setIncome(IncomeGroup.average);

            session.save(person1);

            session.save(person2);

            session.getTransaction().commit();

        } finally {

            if (session.getTransaction().isActive()) {

                session.getTransaction().rollback();

            }

            session.close();

        }

    }
