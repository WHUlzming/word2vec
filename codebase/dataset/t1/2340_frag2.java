    void onMnemonic(TraverseEvent event) {

        char mnemonic = _findMnemonic(text);

        if (mnemonic == '\0') {

            return;

        }

        if (Character.toLowerCase(event.character) != mnemonic) {

            return;

        }

        Composite control = this.getParent();

        while (control != null) {

            Control[] children = control.getChildren();

            int index = 0;

            while (index < children.length) {

                if (children[index] == this) {

                    break;

                }

                index++;

            }

            index++;

            if (index < children.length) {

                if (children[index].setFocus()) {

                    event.doit = true;

                    event.detail = SWT.TRAVERSE_NONE;

                }

            }

            control = control.getParent();

        }

    }
