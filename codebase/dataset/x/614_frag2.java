        byte[] dataToCipher = new byte[hashCodeManager.length + data.length];

        System.arraycopy(hashCodeManager, 0, dataToCipher, 0, hashCodeManager.length);

        System.arraycopy(data, 0, dataToCipher, hashCodeManager.length, data.length);

        CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();
