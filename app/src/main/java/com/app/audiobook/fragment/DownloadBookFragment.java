package com.app.audiobook.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadBookFragment extends BaseFragment{

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
        initCloseButton();
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
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        123);
            } else {
                if (getSelectedChapterFromList().size() != 0) {
                    showProgress();
                    BookManager.addBook(getContext(), audioBook);

                    new DownloadFileAsync(audioBook, downloadedPart).execute(getSelectedChapterFromList().get(0).getUrl());
                } else {
                    Toast.makeText(getContext(), "Выберите хотя бы одну главу", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<Chapter> getSelectedChapterFromList() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        DownloadChapterAdapter adapter = (DownloadChapterAdapter) recyclerView.getAdapter();

        ArrayList<Chapter> chapters = adapter.getSelectedChapter();

        return chapters;
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

    private void hide() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.remove(this).commit();
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        private AudioBook audioBook;
        private int part;

        public DownloadFileAsync(AudioBook audioBook, int part) {
            this.audioBook = audioBook;
            this.part = part;
        }

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("lol", "Starting download");

            int size = getSelectedChapterFromList().size();

            setProgress(downloadedPart, size);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {

                Log.v("lol", "Downloading");
                URL url = new URL(fileUrl[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                String cacheFilePath = Environment.getExternalStorageDirectory() + "/AudioBook/cache/" + audioBook.getId() + "/" + audioBook.getChapters().get(part).getId() + ".lol";

                OutputStream output = new FileOutputStream(cacheFilePath);
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            Log.v("lol", "Downloaded");
            downloadedPart ++;

            int size = getSelectedChapterFromList().size();

            setProgress(downloadedPart, size);

            if (downloadedPart < size) {
                new DownloadFileAsync(audioBook, downloadedPart).execute(getSelectedChapterFromList().get(downloadedPart).getUrl());
            } else {
                Toast.makeText(DownloadBookFragment.this.getContext(), "Успешно загружено", Toast.LENGTH_SHORT).show();
                hide();
            }
        }

    }

    private void setProgress(int current, int max) {
        ProgressBar progressBar = v.findViewById(R.id.progress_bar);
        progressBar.setMax(max);
        progressBar.setProgress(current);

        TextView textView = v.findViewById(R.id.progress_text);
        textView.setText(String.valueOf(current) + " из " + String.valueOf(max));
    }

    private void showProgress() {
        ConstraintLayout progressLayout = v.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        ConstraintLayout progressLayout = v.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.GONE);
    }

}
