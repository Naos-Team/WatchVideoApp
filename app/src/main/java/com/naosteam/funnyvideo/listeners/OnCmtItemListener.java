package com.naosteam.funnyvideo.listeners;

public interface OnCmtItemListener {
    public void onPreEdit(int visibility);
    public void onEdit(int position, String cmt);
    public void onDel(int position);
}
