package com.naosteam.watchvideoapp.listeners;

import java.io.Serializable;

public interface ControlRadioListener extends Serializable {
    public void onNext();
    public void onPrevious();
}
