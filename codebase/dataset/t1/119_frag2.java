                        otherImage = ByteBuffer.allocateDirect(memreq).order(ByteOrder.nativeOrder()).asShortBuffer();

                    } catch (OutOfMemoryError ome) {

                        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, psm.getUnpackAlignment());

                        gl.glPixelStorei(GL.GL_UNPACK_SKIP_ROWS, psm.getUnpackSkipRows());

                        gl.glPixelStorei(GL.GL_UNPACK_SKIP_PIXELS, psm.getUnpackSkipPixels());

                        gl.glPixelStorei(GL.GL_UNPACK_ROW_LENGTH, psm.getUnpackRowLength());

                        gl.glPixelStorei(GL.GL_UNPACK_SWAP_BYTES, (psm.getUnpackSwapBytes() ? 1 : 0));

                        return (GLU.GLU_OUT_OF_MEMORY);

                    }
