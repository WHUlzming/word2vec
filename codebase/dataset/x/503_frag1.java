    public void dealOneFrame() {

        frame.dealOneFrame();

        if (ui != null) {

            ui.uiDealOneFrame();

        }

        if (scene != null) {

            scene.sceneDealOneFrame();

        }

    }
