            if (c1[i].isAssignableFrom(c2[i])) res--; else if (c2[i].isAssignableFrom(c2[i])) res++;

        }

        return res > 0;

    }



    /**

	 * Returns the list of classes of the object 

	 *

	 * @param o an Object

	 */

    public static Class[] getClasses(Object o) {

        Vector vec = new Vector();

        Class cl = o.getClass();

        while (cl != null) {

            vec.add(cl);

            cl = cl.getSuperclass();
