    static Object[] packageParameterFromVarArg(VM_Method targetMethod, VM_Address argAddress) {

        VM_Type[] argTypes = targetMethod.getParameterTypes();

        int argCount = argTypes.length;

        Object[] argObjectArray = new Object[argCount];

        VM_JNIEnvironment env = VM_Thread.getCurrentThread().getJNIEnv();

        VM_Address addr = argAddress;

        for (int i = 0; i < argCount; i++) {

            int loword, hiword;

            hiword = VM_Magic.getMemoryWord(addr);

            addr = addr.add(4);

            if (argTypes[i].isFloatType()) {

                loword = VM_Magic.getMemoryWord(addr);

                addr = addr.add(4);

                long doubleBits = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapFloat((float) (Double.longBitsToDouble(doubleBits)));

            } else if (argTypes[i].isDoubleType()) {

                loword = VM_Magic.getMemoryWord(addr);

                addr = addr.add(4);

                long doubleBits = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapDouble(Double.longBitsToDouble(doubleBits));

            } else if (argTypes[i].isLongType()) {

                loword = VM_Magic.getMemoryWord(addr);

                addr = addr.add(4);

                long longValue = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapLong(longValue);

            } else if (argTypes[i].isBooleanType()) {

                argObjectArray[i] = VM_Reflection.wrapBoolean(hiword);

            } else if (argTypes[i].isByteType()) {

                argObjectArray[i] = VM_Reflection.wrapByte((byte) hiword);

            } else if (argTypes[i].isCharType()) {

                argObjectArray[i] = VM_Reflection.wrapChar((char) hiword);

            } else if (argTypes[i].isShortType()) {

                argObjectArray[i] = VM_Reflection.wrapShort((short) hiword);

            } else if (argTypes[i].isReferenceType()) {

                argObjectArray[i] = env.getJNIRef(hiword);

            } else if (argTypes[i].isIntType()) {

                argObjectArray[i] = VM_Reflection.wrapInt(hiword);

            } else {

                return null;

            }

        }

        return argObjectArray;

    }
