            System.out.println("Error: " + e);

        }

        try {

            long time = System.currentTimeMillis();

            long rand = 0;

            if (secure) {

                rand = mySecureRand.nextLong();

            } else {

                rand = myRand.nextLong();

            }

            sbValueBeforeMD5.append(s_id);

            sbValueBeforeMD5.append(":");

            sbValueBeforeMD5.append(Long.toString(time));

            sbValueBeforeMD5.append(":");

            sbValueBeforeMD5.append(Long.toString(rand));

            valueBeforeMD5 = sbValueBeforeMD5.toString();

            md5.update(valueBeforeMD5.getBytes());

            byte[] array = md5.digest();

            StringBuffer sb = new StringBuffer();

            for (int j = 0; j < array.length; ++j) {

                int b = array[j] & 0xFF;

                if (b < 0x10) sb.append('0');
