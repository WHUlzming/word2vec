            } catch (IOException e) {

            }

        }

        listHolder.setIndex(listHolder.getList().size());

        File srcFile = (File) super.currentObjBeingProcessed;

        if (deleteProcessedFiles) {

            srcFile.delete();

        } else {

            fileUtils.moveFileToDoneLocation(srcFile, srcDoneLocFile.toString());
