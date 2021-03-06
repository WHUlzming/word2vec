    public byte[] writePropertyValue(int da, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data) throws IOException {

        PropertyRW prop = new PropertyRW(da, objIdx, propID, startIdx, noElems);

        synchronized (prop) {

            if ((startIdx < 4096) && (noElems < 16)) {

                synchronized (propValRWWaitings) {

                    propValRWWaitings.add(prop);

                }

                aus.propertyValue_WriteReq(da, pr, hc, objIdx, propID, (int) startIdx, noElems, data, false);

                try {

                    prop.wait(TIMEOUT_TIME);

                } catch (InterruptedException e) {

                }

                if (prop.data == null) {

                    synchronized (propValRWWaitings) {

                        propValRWWaitings.remove(prop);

                    }

                    throw new IOException();

                }

            } else {

                synchronized (propValFWriteWaitings) {

                    propValFWriteWaitings.add(prop);

                }

                aus.propertyValue_FWriteReq(da, pr, hc, objIdx, propID, startIdx, noElems, data, false);

                try {

                    prop.wait(TIMEOUT_TIME);

                } catch (InterruptedException e) {

                }

                if (prop.data == null) {

                    synchronized (propValFWriteWaitings) {

                        propValFWriteWaitings.remove(prop);

                    }

                    throw new IOException();

                }

            }

        }

        if ((prop.data.length != 0) || (noElems == 0)) {

            return prop.data;

        } else {

            return null;

        }

    }
