        if (consumer.isConsumerRunning()) {

            int stopCode = consumer.stopConsumer();

            Integer exitCode = consumer.getExitCode();

            item.log("Consumer Stopped(" + stopCode + ") with exit code:" + exitCode);

            System.out.println(this + " : Consumer Stopped (" + stopCode + ") with exit code:" + exitCode);

        } else {

            Integer exitCode = consumer.getExitCode();

            item.log("Consumer already not running, it exited with:" + exitCode);

            System.out.println(this + " : Consumer already not running, it exited with:" + exitCode);

        }

        producer.decUsageCount();

        int deviceID = producer.getDeviceIndex();

        String deviceString = producer.getCaptureDevice().getID();

        int usageCount = producer.getUsageCount();

        if (usageCount == 0) {

            if (producer.isProducerRunning()) {

                int stopCode = producer.stopProducer();

                Integer exitCode = producer.getExitCode();

                item.log("Producer Stopped(" + stopCode + ") with exit code:" + exitCode);

                System.out.println(this + " : Producer Stopped(" + stopCode + ") with exit code:" + exitCode);

            } else {

                Integer exitCode = producer.getExitCode();

                item.log("Producer already not running, it exited with:" + exitCode);

                System.out.println(this + " : Producer already not running, it exited with:" + exitCode);

                if (exitCode.intValue() == -7) {

                    item.log("Producer return code was for No Data Flowing.");

                    System.out.println(this + " : Return code was for No Data Flowing.");

                    runNoDataErrorTask(producer.getDeviceIndex(), producer.getCaptureDevice().getID());

                }

            }

            producer.getCaptureDevice().setInUse(false);

            CaptureDeviceList devList = CaptureDeviceList.getInstance();

            devList.remProducer(producer.getKey());

            producer = null;

        } else {

            item.log("Producer not stopped, inUseCount(" + usageCount + ")");

            System.out.println(this + " : Producer not stopped, inUseCount(" + usageCount + ")");

        }

        if (item.getState() == ScheduleItem.RESTART) {
