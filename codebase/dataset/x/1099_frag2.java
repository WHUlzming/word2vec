    public tcg.syscontrol.cos.ICosManagedProcess cosGetManagedProcess(short index) throws tcg.syscontrol.cos.CosIndexOutOfBoundException {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("cosGetManagedProcess", true);

                    _os.write_ushort(index);

                    _is = _invoke(_os);

                    tcg.syscontrol.cos.ICosManagedProcess _result = tcg.syscontrol.cos.ICosManagedProcessHelper.read(_is);

                    return _result;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();

                    if (_id.equals("IDL:tcg/syscontrol/cos/CosIndexOutOfBoundException:1.0")) {

                        throw tcg.syscontrol.cos.CosIndexOutOfBoundExceptionHelper.read(_ax.getInputStream());

                    }

                    throw new RuntimeException("Unexpected exception " + _id);

                } finally {

                    this._releaseReply(_is);

                }

            } else {

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cosGetManagedProcess", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");

                ICosProcessManagerOperations _localServant = (ICosProcessManagerOperations) _so.servant;

                tcg.syscontrol.cos.ICosManagedProcess _result;

                try {

                    _result = _localServant.cosGetManagedProcess(index);

                } finally {

                    _servant_postinvoke(_so);

                }

                return _result;

            }

        }

    }
