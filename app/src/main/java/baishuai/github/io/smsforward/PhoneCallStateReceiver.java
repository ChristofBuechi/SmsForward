package baishuai.github.io.smsforward;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.List;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.Group;
import allbegray.slack.webapi.SlackWebApiClient;

/**
 * Created by dream on 2017/9/22.
 */

public class PhoneCallStateReceiver extends BroadcastReceiver {

    private TelephonyManager mTelephonyManager;
    public static boolean isListening = false;
    private SlackWebApiClient mWebApiClient;
    private SlackRealTimeMessagingClient mRtmClient;


    @Override
    public void onReceive(final Context context, Intent intent) {

        mTelephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, final String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
//                        Toast.makeText(context, "CALL_STATE_RINGING" + incomingNumber, Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                final String slackToken = "";
                                final String channel = "";
                                mWebApiClient = SlackClientFactory.createWebApiClient(slackToken);
                                String webSocketUrl = mWebApiClient.startRealTimeMessagingApi().findPath("url").asText();
                                mRtmClient = new SlackRealTimeMessagingClient(webSocketUrl);
                                mRtmClient.connect();
                                List<Group> gList =  mWebApiClient.getGroupList(true);
                                String chid="";
                                for ( Group group : gList) {
                                    if (channel.equals(group.getName())){
                                        chid = group.getId();
                                    }
                                }
                                System.out.println(chid);
                                if(mWebApiClient.meMessage(chid, incomingNumber + ": " + "在拨打您的手机")){
                                    mRtmClient.close();
                                    mWebApiClient.shutdown();
                                }
                            }
                        }).start();
                    }
                        break;
                }
            }
        };

        if(!isListening) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            isListening = true;
        }
    }
}
