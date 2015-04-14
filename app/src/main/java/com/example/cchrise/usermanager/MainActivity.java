package com.example.cchrise.usermanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;




public class MainActivity extends Activity {

    EditText nameTxt;
    List<User> Users = new ArrayList<User>();
    ListView userListView;
    DatabaseHandler dbHandler;
    private String TAG = (String) MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.name);
        userListView = (ListView) findViewById(R.id.listView);
        dbHandler = new DatabaseHandler(getApplicationContext());

        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("List");
        tabSpec.setContent(R.id.tabUserList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                tabHost.setCurrentTab(1);
                populateList();

            }
        });

        userListView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "User clicked", Toast.LENGTH_LONG).show();
            }
        });


        final Button addBtn = (Button) findViewById(R.id.create);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(dbHandler.getUserCount(), String.valueOf(nameTxt.getText()));
                if (!userExists(user)) {
                    dbHandler.createUser(user);
                    Users.add(user);
                    Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " has been added to Users!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " User already exists. Please use a different name." , Toast.LENGTH_SHORT).show();



            }
        });





        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            //Enables the Button when nothing is wrote in the field
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    //-------------Liefert User und schreib diese in die ArrayList Users----//
        List<User> addableUsers = dbHandler.getAllUsers();
        int userCount = dbHandler.getUserCount();

        for (int i = 0 ; i < userCount; i++){
            Users.add(addableUsers.get(i));

        }
        if(addableUsers.isEmpty())
        populateList();
    }


    //-----------UserExist----------------//
    private boolean userExists(User user) {
        String name = user.getName();
        int userCount = Users.size();

        for (int i = 0; i < userCount; i++) {
            if (name.compareToIgnoreCase(Users.get(i).getName()) == 0)
                return true;
        }
        return false;
    }




    //----------------Liste mit Daten befüllen----------------//
    private void populateList(){

        ArrayAdapter<User> adapter = new UserListAdapter();
        userListView.setAdapter(adapter);
    }

    private void addUser(String name) {
        Users.add(new User(0,name));
    }





    private class UserListAdapter extends ArrayAdapter<User> {

        public UserListAdapter(){
            super(MainActivity.this, R.layout.listview_item, Users);
        }

    @Override
        public View getView(int position, View view, ViewGroup parent){
        if(view == null)
            view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

        User currentUser = Users.get(position);
        TextView name = (TextView) view.findViewById(R.id.userName);
        name.setText(currentUser.getName());

        return view;
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
