    public static void setUp() {

        System.out.println("In Setup : " + PoolManager.isIntialized());

        try {

            if (!PoolManager.isIntialized()) {

                PoolStartupPropertyFile ps = new PoolStartupPropertyFile();

                ps.init();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
