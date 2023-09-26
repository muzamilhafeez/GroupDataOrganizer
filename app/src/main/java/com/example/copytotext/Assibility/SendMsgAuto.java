package com.example.copytotext.Assibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.accessibilityservice.AccessibilityServiceInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class SendMsgAuto extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if(getRootInActiveWindow()==null){
            return;
        }
        //get root mode
        AccessibilityNodeInfoCompat  rootNodeInfo= AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

        //
        List<AccessibilityNodeInfoCompat> messagerootNodeList=rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        if(messagerootNodeList.isEmpty() || messagerootNodeList==null){
            return;
        }
        AccessibilityNodeInfoCompat msgFeid= messagerootNodeList.get(0);
        //check msg empty or surfix
        if(msgFeid==null || msgFeid.getText().length()==0|| !msgFeid.getText().toString().endsWith("  ")){
            return;
        }
        //msg send to whatsapp
        List<AccessibilityNodeInfoCompat> sendmessagerootNodeList=rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if(messagerootNodeList.isEmpty() || messagerootNodeList==null){
            return;
        }

        AccessibilityNodeInfoCompat sendmsgFeid= sendmessagerootNodeList.get(0);
        //check send button not show
        if(!sendmsgFeid.isVisibleToUser()){
            return;
        }
        //fire msg button click send
        sendmsgFeid.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        //go back clicking back button
        try{
            Thread.sleep(2000);//some  device  can not handle call back
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(2000);
        }catch (InterruptedException ignored){
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    @Override
    public void onInterrupt() {

    }
}
