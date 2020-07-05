package com.getstream.sdk.chat.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.getstream.sdk.chat.R;
import com.getstream.sdk.chat.StreamChat;
import com.getstream.sdk.chat.utils.exomedia.ui.widget.VideoView;

/**
 * An Activity playing attachments such as stream_ic_audio and videos.
 */
public class AttachmentMediaActivity extends AppCompatActivity {

    VideoView videoView;
    ImageView iv_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_activity_attachment_media);
        videoView = findViewById(R.id.videoView);
        iv_audio = findViewById(R.id.iv_audio);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "Something error!", Toast.LENGTH_SHORT);
            return;
        }
        if (type.contains("audio"))
            iv_audio.setVisibility(View.VISIBLE);
        else
            iv_audio.setVisibility(View.GONE);

        playVideo(StreamChat.getInstance(this).getUploadStorage().signFileUrl(url));
    }

    /**
     * Play media file with url
     *
     * @param url media url
     */
    public void playVideo(String url) {
        videoView.setVideoURI(Uri.parse(StreamChat.getInstance(this).getUploadStorage().signFileUrl(url)));
        videoView.setOnPreparedListener(() -> videoView.start());
    }
}
