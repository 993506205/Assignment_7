package com.example.assignment_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Person> personList;
    private AutoCompletePersonAdapter adapter_actv;
    private static final String FILE_NAME = "catalog.txt";
    private static final String ROOT = Environment.getExternalStorageDirectory().toString();
    private String fname;
    private String lname;
    private String phone;
    private String ed_lvl;
    private String hobbies;

    private Button btn_add_new;

    private ListView list;
    private AutoCompleteTextView actv;

    private int viewBackgroundColor;
    private Drawable drawable;
    private SharedPreferences sharedPreferences;
    private String prefsFileName;

    private final int MENU_COLOR_WHITE = 0;
    private final int MENU_COLOR_DEFAULT = 1;
    private final int MENU_COLOR_GREEN = 2;
    private final int MENU_COLOR_GRAY = 3;
    private final int MENU_COLOR_BLUE = 4;
    private final int MENU_COLOR_RED = 5;

    private final int MENU_TEXT_BLACK = 6;
    private final int MENU_TEXT_RED = 7;
    private final int MENU_TEXT_YELLOW = 8;

    private final int MENU_FONT_SIZE_16 = 9;
    private final int MENU_FONT_SIZE_22 = 10;
    private final int MENU_FONT_SIZE_30 = 11;

    private final int MENU_TYPE_BOLD = 12;
    private final int MENU_TYPE_NORMAL = 13;
    private final int MENU_TYPE_LIGHT = 14;

    private final int MENU_LOCATION_ALL = 15;
    private final int MENU_LOCATION_INTERNAL = 16;
    private final int MENU_LOCATION_EXTERNAL = 17;

    private static final String BACKGROUND_COLOR = "bg_color";
    private static final String FONT_COLOR = "font_color";
    private static final String FONT_SIZE = "font_size";
    private static final String FONT_TYPE = "font_type";

    private int fontSize = 16;
    private String textColor = "#000000";
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.main_layout);


        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mainLayout.setOnCreateContextMenuListener(this);
        drawable = view.getBackground();
        if(drawable instanceof  ColorDrawable)
            viewBackgroundColor = ((ColorDrawable)drawable).getColor();

        btn_add_new = (Button) findViewById(R.id.btn_add_new);
        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPeronActivity();
            }
        });

        Intent intent = getIntent();
        fname = intent.getStringExtra(AddPersonActivity.EXTRA_FNAME);
        lname = intent.getStringExtra(AddPersonActivity.EXTRA_LNAME);
        phone = intent.getStringExtra(AddPersonActivity.EXTRA_PHONE);
        ed_lvl = intent.getStringExtra(AddPersonActivity.EXTRA_EDLVL);
        hobbies = intent.getStringExtra(AddPersonActivity.EXTRA_HOBBIES);
        boolean isInternel = intent.getBooleanExtra(AddPersonActivity.EXTRA_LOCATION, false);
        fillPersonList();

        if(fname != null
                && lname != null
                && phone != null
                && ed_lvl != null
                && hobbies != null){
            if(isInternel){
                saveInternal();
            }else{
                saveExternal();
            }
        }
        personList.clear();
        loadInternal();
        readExternal();



        adapter_actv = new AutoCompletePersonAdapter(this, personList,fontSize,textColor,typeface);
        actv = findViewById(R.id.actv);
        list =(ListView)findViewById(R.id.person_list);
        if(!personList.isEmpty()){
            actv.setAdapter(adapter_actv);
            list.setAdapter(adapter_actv);
        }

        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (MainActivity.this).adapter_actv.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fillPersonList(){
        personList = new ArrayList<Person>();
    }

    public void openAddPeronActivity(){
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }


    /**
     * Create Options Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        createMenu(menu);

        SharedPreferences loadedSharedPrefs = getSharedPreferences(prefsFileName, MODE_PRIVATE);
        int menuItemsSelected_bg = loadedSharedPrefs.getInt(BACKGROUND_COLOR, 0);
        int menuItemsSelected_tc = loadedSharedPrefs.getInt(FONT_COLOR, 0);
        int menuItemsSelected_fs = loadedSharedPrefs.getInt(FONT_SIZE, 0);
        int menuItemsSelected_ft = loadedSharedPrefs.getInt(FONT_TYPE, 0);
        SubMenu bg_color = menu.findItem(R.id.menu_bgColor).getSubMenu();
        SubMenu text_color = menu.findItem(R.id.menu_textColor).getSubMenu();
        SubMenu font_size = menu.findItem(R.id.menu_fontSize).getSubMenu();
        SubMenu font_type = menu.findItem(R.id.menu_font).getSubMenu();
        onOptionsItemSelected(bg_color.getItem(menuItemsSelected_bg));
        onOptionsItemSelected(text_color.getItem(menuItemsSelected_tc));
        onOptionsItemSelected(font_size.getItem(menuItemsSelected_fs));
        onOptionsItemSelected(font_type.getItem(menuItemsSelected_ft));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        sharedPreferences = getSharedPreferences(prefsFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (item.getItemId()){
            case MENU_COLOR_WHITE:
                getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                editor.putInt(BACKGROUND_COLOR, item.getItemId());
                break;
            case MENU_COLOR_DEFAULT:
                getWindow().getDecorView().setBackgroundColor(viewBackgroundColor);
                editor.putInt(BACKGROUND_COLOR, item.getItemId());
                break;
            case MENU_COLOR_GREEN:
                //green
                getWindow().getDecorView().setBackgroundColor(Color.rgb(52,235,174));
                editor.putInt(BACKGROUND_COLOR, item.getItemId());
                break;
            case MENU_COLOR_GRAY:
                //gray
                getWindow().getDecorView().setBackgroundColor(Color.rgb(188,180,194));
                editor.putInt(BACKGROUND_COLOR, item.getItemId());
                break;
            case MENU_COLOR_BLUE:
                //blue
                getWindow().getDecorView().setBackgroundColor(Color.rgb(126,144,237));
                editor.putInt(BACKGROUND_COLOR, item.getItemId());
                break;
            case MENU_COLOR_RED:
                //red
                getWindow().getDecorView().setBackgroundColor(Color.rgb(237,102,109));
                editor.putInt(BACKGROUND_COLOR, item.getItemId());
                break;
            case MENU_TEXT_BLACK:
                textColor = "#000000";
                btn_add_new.setTextColor(Color.parseColor(textColor));
                actv.setTextColor(Color.parseColor(textColor));
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_COLOR, item.getItemId()-6);
                break;
            case MENU_TEXT_RED:
                textColor = "#ff4d4d";
                btn_add_new.setTextColor(Color.parseColor(textColor));
                actv.setTextColor(Color.parseColor(textColor));
                adapter_actv = new AutoCompletePersonAdapter(this, personList,fontSize,textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_COLOR, item.getItemId()-6);
                break;
            case MENU_TEXT_YELLOW:
                textColor = "#ffff00";
                btn_add_new.setTextColor(Color.parseColor(textColor));
                actv.setTextColor(Color.parseColor(textColor));
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_COLOR, item.getItemId()-6);
                break;
            case MENU_FONT_SIZE_16:
                fontSize = 16;
                btn_add_new.setTextSize(fontSize);
                actv.setTextSize(fontSize);
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_SIZE, item.getItemId()-9);
                break;
            case MENU_FONT_SIZE_22:
                fontSize = 22;
                btn_add_new.setTextSize(fontSize);
                actv.setTextSize(fontSize);
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_SIZE, item.getItemId()-9);
                break;
            case MENU_FONT_SIZE_30:
                fontSize = 30;
                btn_add_new.setTextSize(fontSize);
                actv.setTextSize(fontSize);
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_SIZE, item.getItemId()-9);
                break;
            case MENU_TYPE_NORMAL:
                typeface = ResourcesCompat.getFont(this, R.font.opensans_regular);
                btn_add_new.setTypeface(typeface);
                actv.setTypeface(typeface);
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_TYPE, item.getItemId()-12);
                break;
            case MENU_TYPE_BOLD:
                typeface = ResourcesCompat.getFont(this, R.font.opensans_bold);
                btn_add_new.setTypeface(typeface);
                actv.setTypeface(typeface);
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_TYPE, item.getItemId()-12);
                break;
            case MENU_TYPE_LIGHT:
                typeface = ResourcesCompat.getFont(this, R.font.opensans_light);
                btn_add_new.setTypeface(typeface);
                actv.setTypeface(typeface);
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                editor.putInt(FONT_TYPE, item.getItemId()-12);
                break;
            case MENU_LOCATION_INTERNAL:
                personList.clear();
                loadInternal();
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                break;
            case MENU_LOCATION_EXTERNAL:
                personList.clear();
                readExternal();
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                break;
            case MENU_LOCATION_ALL:
                personList.clear();
                readExternal();
                loadInternal();
                adapter_actv = new AutoCompletePersonAdapter(this, personList, fontSize, textColor,typeface);
                if(!personList.isEmpty()){
                    actv.setAdapter(adapter_actv);
                    list.setAdapter(adapter_actv);
                }
                break;
        }
        editor.commit();
        return super.onOptionsItemSelected(item);
    }

    /**
     * create menu items
     * @param menu
     */
    private void createMenu(Menu menu){
        SubMenu bg_color = menu.findItem(R.id.menu_bgColor).getSubMenu();
        bg_color.clear();
        bg_color.add(0, MENU_COLOR_WHITE, 0, "White");
        bg_color.add(0, MENU_COLOR_DEFAULT, 0, "Clear");
        bg_color.add(0, MENU_COLOR_GREEN, 0, "Green");
        bg_color.add(0, MENU_COLOR_GRAY, 0, "Gray");
        bg_color.add(0, MENU_COLOR_BLUE, 0, "Blue");
        bg_color.add(0, MENU_COLOR_RED, 0, "Red");

        SubMenu text_color = menu.findItem(R.id.menu_textColor).getSubMenu();
        text_color.clear();
        text_color.add(0, MENU_TEXT_BLACK, 0, "Black");
        text_color.add(0, MENU_TEXT_RED, 0, "Red");
        text_color.add(0, MENU_TEXT_YELLOW, 0, "Yellow");

        SubMenu font_size = menu.findItem(R.id.menu_fontSize).getSubMenu();
        font_size.clear();
        font_size.add(0, MENU_FONT_SIZE_16, 0, "16");
        font_size.add(0, MENU_FONT_SIZE_22, 0, "22");
        font_size.add(0, MENU_FONT_SIZE_30, 0, "30");

        SubMenu font = menu.findItem(R.id.menu_font).getSubMenu();
        font.clear();
        font.add(0,MENU_TYPE_BOLD,0,"Bold");
        font.add(0,MENU_TYPE_NORMAL,0,"Normal");
        font.add(0,MENU_TYPE_LIGHT,0,"Light");

        SubMenu location = menu.findItem(R.id.menu_location).getSubMenu();
        location.clear();
        location.add(0,MENU_LOCATION_ALL,0,"Catalog All");
        location.add(0,MENU_LOCATION_INTERNAL,0,"Catalog Internal");
        location.add(0,MENU_LOCATION_EXTERNAL,0,"Catalog External");
    }

    /**
     * Save file Internal
     */
    private void saveInternal(){
        String text = fname + ","
                + lname + ","
                + phone + ","
                + ed_lvl + ","
                + hobbies + "\n";
        FileOutputStream fos = null;
        try{
            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            fos.write(text.getBytes());
            Toast.makeText(this, "Person Saved.", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Load file Internal
     */
    private void loadInternal(){
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            String[] personInfo;
            //personList.clear();

            while((text = br.readLine()) != null){
                personInfo = text.split(",");

                personList.add(new Person(personInfo[0], personInfo[1], personInfo[2], personInfo[3], personInfo[4]));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * is External Storage Writable
     */
    private boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Yes, it is writable!");
            return true;
        }else {
            return false;
        }
    }

    /**
     * is External Storage Readable
     */
    private boolean isExternalStorageReadable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            Log.i("State", "Yes, it is readable!");
            return true;
        }else {
            return false;
        }
    }

    /**
     * save file external
     */
    private void saveExternal(){
        if(isExternalStorageWritable()){
            String text = fname + ","
                    + lname + ","
                    + phone + ","
                    + ed_lvl + ","
                    + hobbies + "\n";
            File myDir = new File(ROOT + "/saved_catalog");
            if(!myDir.exists()){
                myDir.mkdirs();
            }
            File catalogFile = new File(myDir, FILE_NAME);
            try{
                FileOutputStream fos = new FileOutputStream(catalogFile, true);
                fos.write(text.getBytes());
                fos.close();

                Toast.makeText(this, "Person Saved.", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "Cannot Write to External Storage.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * read file External
     */
    private void readExternal(){
        if(isExternalStorageReadable()){
            String text;
            String[] personInfo;
            File myDir = new File(ROOT + "/saved_catalog");
            if(!myDir.exists()){
                myDir.mkdirs();
            }
            try{
                File catalogFile = new File(myDir, FILE_NAME);
                FileInputStream fis = new FileInputStream(catalogFile);
                if(fis != null){
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    //personList.clear();

                    while((text = br.readLine()) != null){
                        personInfo = text.split(",");

                        personList.add(new Person(personInfo[0], personInfo[1], personInfo[2], personInfo[3], personInfo[4]));
                    }
                    fis.close();
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this, "Cannot Read from External Storage.", Toast.LENGTH_SHORT).show();
        }
    }

}
