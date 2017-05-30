package com.cpe307.swapacado.swapacado;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    protected String uniqueID;

    //Make the buttons at the bottom of the screen active
    private void attachMenuButtonHandlers() {
        View homeMenuBox = this.findViewById(R.id.homeMenuBar);
        homeMenuBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To remove
                Toast.makeText(getApplicationContext(), "Home was clicked!!!", Toast.LENGTH_SHORT).show();
            }
        });

        View searchMenuBox = this.findViewById(R.id.searchMenuBar);
        searchMenuBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Search was clicked!!!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                //intent.putExtra("uniqueID", uniqueID);
                //startActivity(intent);
            }
        });

        View profileMenuBox = this.findViewById(R.id.profileMenuBar);
        profileMenuBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                uniqueID = "123";
                intent.putExtra("uniqueID", uniqueID);
                startActivity(intent);
                finish();
            }
        });
    }

    private void adjustTitleBar() {
        View title = this.findViewById(R.id.homeTitle);
        View plus = this.findViewById(R.id.plusButton);
        View messageIcon = this.findViewById(R.id.messageIcon);

        int titleHeight = title.getLayoutParams().height;

        //Set the plus' height and width to the dimensions of the height of the title
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(titleHeight,titleHeight);
        plus.setLayoutParams(layoutParams);
        messageIcon.setLayoutParams(layoutParams);
    }

    private void configureScreen()
    {
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        configureScreen();

        uniqueID = this.getIntent().getStringExtra("uniqueID");

        attachMenuButtonHandlers();
        adjustTitleBar();

        PostDatabase.refreshAllPosts();

        ListView myListView = (ListView) findViewById(R.id.home_listview);
        Post [] data = getPosts();
        myListView.setAdapter(new CustomAdapter(this.getApplicationContext(), data));
    }
    //Should change the list adapter according to what is in the
    private void filterByWhatIWant()
    {
        String [] wants = getHaves();
        Post [] currentPosts = getPosts();
        ArrayList<Post> filtered = new ArrayList<Post>();
        for(Post aPost : currentPosts)
        {
            boolean canAdd = false;
            for(String query : wants)
            {
                if(!canAdd)
                {
                    canAdd = aPost.matchesHaves(query);
                }
            }
            if(canAdd)
            {
                filtered.add(aPost);
            }
        }
        Post [] filteredArray = filtered.toArray(new Post[filtered.size()]);
        ListView myListView = (ListView) findViewById(R.id.home_listview);
        myListView.setAdapter(new CustomAdapter(this.getApplicationContext(), filteredArray));
    }

    private Post[] getPosts() {
        Post [] data = new Post[50];
        for(int ind = 0; ind<data.length; ind++)
        {
            data[ind] = new Post();
        }
        return data;
    }

    //Display in list view only people asking for things i have
    public void filterByWhatIHave()
    {
        String [] haves = getHaves();
        Post [] currentPosts = getPosts();
        ArrayList<Post> filtered = new ArrayList<Post>();
        for(Post aPost : currentPosts)
        {
            boolean canAdd = false;
            for(String query : haves)
            {
                if(!canAdd)
                {
                    canAdd = aPost.matchesWants(query);
                }
            }
            if(canAdd)
            {
                filtered.add(aPost);
            }
        }
        Post [] filteredArray = filtered.toArray(new Post[filtered.size()]);
        Log.d("Ahluwalia", "TESTNG" + filteredArray.length);
        ListView myListView = (ListView) findViewById(R.id.home_listview);
        myListView.setAdapter(new CustomAdapter(this.getApplicationContext(), filteredArray));

    }

    public void filterByWhatIHaveAndWant()
    {
        String [] haves = getHaves();
        String [] wants = getWants();
        String [] concat = new String [wants.length + haves.length];
        for(int ind = 0; ind < haves.length; ind++)
        {
            concat[ind] = haves[ind];
        }

        for(int ind = 0; ind < wants.length; ind++)
        {
            concat[haves.length + ind] = wants[ind];
        }

        addIfMatchesConditionsInArray(haves);
    }

    //
    private void addIfMatchesConditionsInArray(String [] conditions)
    {
        Post [] currentPosts = getPosts();
        ArrayList<Post> filtered = new ArrayList<Post>();
        for(Post aPost : currentPosts)
        {
            boolean canAdd = false;
            for(String query : conditions)
            {
                if(!canAdd)
                {
                    canAdd = aPost.matchesSearchQuery(query);
                }
            }
            if(canAdd)
            {
                filtered.add(aPost);
            }
        }

        Post [] filteredArray = filtered.toArray(new Post[filtered.size()]);
        Log.d("Ahluwalia", "TESTNG" + filteredArray.length);
        ListView myListView = (ListView) findViewById(R.id.home_listview);
        myListView.setAdapter(new CustomAdapter(this.getApplicationContext(), filteredArray));
    }

    //Get a string array of items that the user wants
    //Currently is a stub method
    private String[] getHaves() {
        return new String []{"Chips", "Wine", "Coke"};
    }
    //Stub method, need to fuilly implement later
    private String[] getWants() {
        return new String [] {"Eggs", "Milk", "Cereal"};
    }

    public void toggleHaves(View view) {
        checkboxWork();

    }

    public void toggleWants(View view) {
        checkboxWork();
    }
    public void filterPerfectTrades()
    {
        Post [] posts = getPosts();
        ArrayList<Post> filtered = new ArrayList<>();
        //TODO
    }

    private void checkboxWork() {
        CheckBox ifIHave = (CheckBox) findViewById(R.id.home_toggleHave);
        CheckBox ifIWant = (CheckBox) findViewById(R.id.home_toggleWant);

        boolean onlyThingsIWant = ifIHave.isChecked();
        boolean onlyThingsIHave = ifIWant.isChecked();

        if(onlyThingsIHave && onlyThingsIWant)
        {
            filterPerfectTrades();
        }
        else if(onlyThingsIHave)
        {
            filterByWhatIHave();
        }
        else if(onlyThingsIWant)
        {
            filterByWhatIWant();
        }
        else
        {
            ListView myListView = (ListView) findViewById(R.id.home_listview);
            Post [] data = getPosts();
            myListView.setAdapter(new CustomAdapter(this.getApplicationContext(), data));
        }

    }

}