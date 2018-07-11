    public static void main(String[] args) {

        Properties properties = new Properties();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default", properties);

        try {

            System.out.println("*** insert and detach ***");

            insertanddetach(entityManagerFactory);

            System.out.println("*** change ***");

            changeDetachedObject();

            System.out.println("*** attach and commit ***");

            attach(entityManagerFactory);

            System.out.println("*** query ***");

            query(entityManagerFactory);

            System.out.println("*** delete ***");

            delete(entityManagerFactory);

        } finally {

            entityManagerFactory.close();

            System.out.println("*** finished ***");

        }

    }
