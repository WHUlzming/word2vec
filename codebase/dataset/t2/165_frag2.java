        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);

        System.arraycopy(array, 0, newArray, 0, array.length);

        newArray[array.length] = obj;
