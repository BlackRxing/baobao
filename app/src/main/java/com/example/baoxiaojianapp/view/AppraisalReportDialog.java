package com.example.baoxiaojianapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.baoxiaojianapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppraisalReportDialog extends Dialog {

    public AppraisalReportDialog(@NonNull Context context) {
        super(context);
        this.setContentView(R.layout.repor_dialog);
    }

    public AppraisalReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setContentView(R.layout.repor_dialog);

    }

    protected AppraisalReportDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.setContentView(R.layout.repor_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
