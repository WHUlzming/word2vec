        String propertyName = "PropertyFour";

        Class<MockJavaBean> beanClass = MockJavaBean.class;

        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);

        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });

        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
