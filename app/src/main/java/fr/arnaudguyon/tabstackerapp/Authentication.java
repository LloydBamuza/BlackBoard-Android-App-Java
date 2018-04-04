package fr.arnaudguyon.tabstackerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import static fr.arnaudguyon.tabstackerapp.SignInActivity.*;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.util.List;

/**
 * Created by lloyd on 2018/04/04.
 */

public interface Authentication
{
    int INTERNET_PERM_REQ_CODE = 100;
    int STORAGE_WR_PERM_REQ_CODE = 101; //storage write request code
    int STORAGE_R_PERM_REQ_CODE = 102; //storage read request code
    String SENDBIRD_APP_ID = "C1986EC7-1C74-44B9-92FE-3EE2705872DA";



    @SuppressLint("WrongConstant")
    default void requestPermissions(Context context, Activity activity)
    {

        PackageManager pckgManager = context.getPackageManager();

        //get permissions
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.INTERNET},INTERNET_PERM_REQ_CODE);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_R_PERM_REQ_CODE);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_WR_PERM_REQ_CODE);

        //update permissions' status
        hasStorageWPerm = pckgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context.getPackageName());
        hasStorageRPerm = pckgManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, context.getPackageName());
        hasInternetPerm = pckgManager.checkPermission(Manifest.permission.INTERNET, context.getPackageName());
    }

    default boolean login(Context context, String uName, String pwd)
    {
        final boolean[] success = new boolean[1]; //work-around on requirement for inner-class variable use to be final
        new Runnable()
        {
            @Override
            public void run()
            {
                SendBird.connect(uName, new SendBird.ConnectHandler()
                {
                    @Override
                    public void onConnected(User user, SendBirdException e)
                    {
                        if(e != null)
                        {
                            Toast.makeText(context,"Error logging in", Toast.LENGTH_LONG);
                            success[0] = false;
                        }
                        else
                        {
                            Toast.makeText(context,"Login successful", Toast.LENGTH_SHORT);
                            success[0] = true;

                        }
                    }
                });

            }
        }.run();

        return success[0];
    }

    default void initSDK(Context context)
    {
        new Runnable()
        {
            @Override
            public void run()
            {

                SendBird.init(SENDBIRD_APP_ID, context);
            }
        }.run();

    }

    static void enterChannel(Context context)
    {
        new Runnable()
        {
            @Override
            public void run() {
                OpenChannel.getChannel(SignUpActivity.TEST_OPEN_CHANNEL_ID, new OpenChannel.OpenChannelGetHandler() {
                    @Override
                    public void onResult(OpenChannel openChannel, SendBirdException e) {
                        if (e != null) {
                            // Error.
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }


                        openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                            @Override
                            public void onResult(SendBirdException e) {
                                if (e != null) {
                                    // Error.
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                }


                            }
                        });

                        //get reference to open channel, Set to static variable
                        SignInActivity.openChannel = openChannel;
                    }
                });
            }
        }.run();

    }

    default boolean isRegisteredUser(Context context, String usrEmail)
    {
        final boolean[] result = {false};
        new Runnable()
        {
            @Override
            public void run()
            {
                UserListQuery userListQuery = openChannel.createParticipantListQuery();
                userListQuery.next(new UserListQuery.UserListQueryResultHandler() {
                    @Override
                    public void onResult(List<User> list, SendBirdException e) {
                        if (e != null) {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                            return;
                        }

                        //search for user

                        for(User user : list)
                        {
                            if(user.getUserId() == usrEmail)
                            {
                                result[0] = true;
                                break;
                            }
                        }
                    }
                });
            }
        }.run();


        return result[0];
    }


}
