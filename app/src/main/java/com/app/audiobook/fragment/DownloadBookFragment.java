package com.app.audiobook.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.DownloadChapterAdapter;
import com.app.audiobook.audio.BookManager;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Chapter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DownloadBookFragment extends BaseFragment {

    private View v;
    private AudioBook audioBook;

    private int downloadedPart = 0;

    public DownloadBookFragment(AudioBook audioBook) {
        this.audioBook = audioBook;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_download_book, container, false);

        initInterface();

        return v;
    }

    private void initInterface() {
        initRecyclerView();
        initDownloadButton();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        DownloadChapterAdapter adapter = new DownloadChapterAdapter();
        adapter.setChapters(audioBook.getChapters());

        recyclerView.setAdapter(adapter);
    }

    private void initDownloadButton() {
        ConstraintLayout download = v.findViewById(R.id.buttonDownload);
        download.setOnClickListener(v1 -> {
            int permissionStatus1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionStatus2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionStatus1 != PackageManager.PERMISSION_GRANTED || permissionStatus2 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        123);
            } else {

                BookManager.addBook(getContext(), audioBook);

                Toast.makeText(getContext(), "Загрузка начата", Toast.LENGTH_SHORT).show();

                RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

                DownloadChapterAdapter adapter = (DownloadChapterAdapter) recyclerView.getAdapter();

                ArrayList<Chapter> chapters = adapter.getSelectedChapter();

                for (int i = 0; i < chapters.size(); i ++) {
                    Chapter chapter = chapters.get(i);

                    int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadFile(chapter.getUrl(), finalI, audioBook.getChapters().size());
                        }
                    }).start();

                }
            }
        });
    }

    private void initCloseButton() {
        ConstraintLayout close = v.findViewById(R.id.back);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    public void downloadFile(String url, int part, int total) {
        try {
            URL u = new URL(url);
            InputStream is = u.openStream();
            DataInputStream dis = new DataInputStream(is);
            byte[] buffer = new byte[1024];
            int length;

            String cacheFilePath = Environment.getExternalStorageDirectory() + "/AudioBook/cache/" + audioBook.getId() + "/" + audioBook.getChapters().get(part).getId() + ".lol";

            File cachedFile = new File(cacheFilePath);

            if (!cachedFile.exists()) {
                cachedFile.getParentFile().mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(cachedFile);
            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            Log.v("lol", "part loaded : " + String.valueOf(part));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadedPart ++;
                    if (total == downloadedPart) {
                        Toast.makeText(getContext(), "Загружено", Toast.LENGTH_SHORT).show();
                        hide();
                    }
                }
            });

        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }

    private void hide() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.remove(this).commit();
    }

}
