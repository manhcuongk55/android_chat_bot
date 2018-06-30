package viettel.cyberspace.assitant.utils;

import android.media.AudioTrack;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by duy on 29/06/2018.
 */

public class VoiceUtils {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void request(String text) {
        // TODO Auto-generated method stub
        int code = -1;

        String urlString = "http://203.113.152.90/hmm-stream/syn";

        // urlString= urlString+"?data="+text +"&voices=doanngocle.htsvoice&key=K9W6tNTeUuwrkyYARkAmzJ94D9vUR2Qdo5YwVI7D";

        long time1 = System.currentTimeMillis();
        Log.i("duypq33", "time1=" + time1);
        StringBuffer chaine = new StringBuffer("");
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
           /* connection.setRequestProperty("data", text);
            connection.setRequestProperty("voices", "doanngocle.htsvoice");
            connection.setRequestProperty("key", "K9W6tNTeUuwrkyYARkAmzJ94D9vUR2Qdo5YwVI7D");*/


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("data", text)
                    .appendQueryParameter("voices", "doanngocle.htsvoice")
                    .appendQueryParameter("key", "K9W6tNTeUuwrkyYARkAmzJ94D9vUR2Qdo5YwVI7D");
            String query = builder.build().getEncodedQuery();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            connection.setRequestMethod("POST");
            connection.connect();

            code = connection.getResponseCode();
            Log.i("duypq33", "code=" + code);
            if (code == 200) {

                InputStream input = connection.getInputStream();
                Log.i("duypq33", "time2=" + (System.currentTimeMillis() - time1));
                BufferedInputStream in = new BufferedInputStream(input);
                byte[] byte_buff = new byte[8000];
                int nread;
                AudioTrack player;
                player = MyPlayAudio.getInstance().getPlayer();
                player.play();

                while ((nread = in.read(byte_buff)) != -1) {
                    player.write(byte_buff, 0, nread);
                }

            }
            connection.disconnect();
        } catch (IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }
    }
}
