package com.example.flashcard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public int pos;
    private Gson gson = new Gson();
    private FlashcardInfo[] flashcard;
    private List<FlashcardInfo> flashcardList;
    private Comparator<FlashcardInfo> comparator;
    private ListView listView;
    private FloatingActionButton newButton;
    private FlashcardAdapter fcAdapter;
    //list attributes
    private TextView title;
    private TextView numOfFlashcards;
    private SeekBar priority;
    private TextView dueDate;
    private ToggleButton active;
    public static final String EXTRA_FLASHCARD = "selectedFlashcard";
    public static final String EXTRA_POSOFFLASHCARD = "position in list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wireWidgets();
        boolean isFilePresent = isFilePresent(ListActivity.this, "storage.json");
        if(isFilePresent) {
            String jsonString = read(ListActivity.this, "storage.json");
            //do the json parsing here and do the rest of functionality of app
            flashcard = gson.fromJson(jsonString, FlashcardInfo[].class);
            flashcardList = Arrays.asList(flashcard);

        } else {
            boolean isFileCreated = create(ListActivity.this, "storage.json", "{}");
            if(isFileCreated) {
                //proceed with storing the first todo  or show ui
            } else {
                //show error or try again.
            }
        }

        comparator = new Comparator<FlashcardInfo>() {
            @Override
            public int compare(FlashcardInfo fc1, FlashcardInfo fc2) {
                return fc1.getFlashcardTitle().compareTo(fc2.getFlashcardTitle());
            }
        };
        fcAdapter = new FlashcardAdapter(flashcardList);
        listView.setAdapter(fcAdapter);
        setListeners();

    }

    private void setListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                Intent flashcardIntent = new Intent(ListActivity.this,FlashcardActivity.class);
                flashcardIntent.putExtra(EXTRA_FLASHCARD,flashcardList.get(position));
                flashcardIntent.putExtra(EXTRA_POSOFFLASHCARD,pos);
                startActivity(flashcardIntent);
            }
        });
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException ignored) {

        }
        return outputStream.toString();
    }

    private void wireWidgets() {
        listView = findViewById(R.id.main_listView_flashcardList);
        newButton = findViewById(R.id.main_floatingActionButton_add);
        title = findViewById(R.id.listMaterial_textView_flashcardTitleString);
        numOfFlashcards = findViewById(R.id.listMaterial_textView_numOfFlashcards);
        priority = findViewById(R.id.listMaterial_seekBar_priority);
        dueDate = findViewById(R.id.listMaterial_textView_dueDate);
        active = findViewById(R.id.listMaterial_toggleButton_activeState);

    }

    private class FlashcardAdapter extends ArrayAdapter<FlashcardInfo> {
        private List<FlashcardInfo> flashcardList;
        private int position;
        public FlashcardAdapter(List<FlashcardInfo> flashcardList) {
            super(ListActivity.this,-1, ListActivity.this.flashcardList);
            this.flashcardList = flashcardList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            this.position = position;
            //Inflating Layout
            LayoutInflater inflater = getLayoutInflater();
            //Checking if convertView is null, replacing if needed
            if (convertView == null){
                convertView = inflater.inflate(R.layout.widget_listmaterial, parent, false);
            }
            //wiring widgets
            title = convertView.findViewById(R.id.listMaterial_textView_flashcardTitleString);
            numOfFlashcards = convertView.findViewById(R.id.listMaterial_textView_numOfFlashcards);
            dueDate = convertView.findViewById(R.id.listMaterial_textView_dueDate);
            priority = convertView.findViewById(R.id.listMaterial_seekBar_priority);
            //setting values
            title.setText(flashcardList.get(position).getFlashcardTitle());
            numOfFlashcards.setText(flashcardList.get(position).getNumOfFlashcards());
            dueDate.setText(flashcardList.get(position).getDueDate());
            priority.setProgress(flashcardList.get(position).getPriority());
            return convertView;
        }
    }
    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }
    private boolean create(Context context, String fileName, String jsonString){
        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }


}
