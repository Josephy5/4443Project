package com.example.final4443project;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
/**
 * One of the classes/code within the colorpicker package that the painting app's main activity uses
 *
 * Reason why it is here as a java class instead being imported in gradle is because the gradle system
 * couldn't import it from the gradle setting, stating that it is not there. Thus, we had to import the
 * whole package here as project files
 *
 * Code below is based on the github repo for the Kristiyan Petrov's colorpicker package
 * https://github.com/kristiyanP/colorpicker
 */
public class CustomDialog extends AppCompatDialog {
    private View view;

    public CustomDialog(Context context, View layout) {
        super(context);
        view = layout;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }
}
