                } catch (NoSuchAlgorithmException ex) {

                    Logger.getLogger(SignerAPI.class.getName()).log(Level.SEVERE, null, ex);

                } catch (UnrecoverableKeyException ex) {

                    Logger.getLogger(SignerAPI.class.getName()).log(Level.SEVERE, null, ex);

                }

                return sign;
