        @Override

        public void writeFloat(float v) {

            try {

                output.writeFloat(v);

            } catch (IOException impossible) {

                throw new AssertionError(impossible);

            }

        }
