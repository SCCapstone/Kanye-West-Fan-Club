package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class MatchDetails extends AppCompatActivity{

    ScrollView matchDetailsContainer;

//    String myValue = MainActivity.getMyString();
    //Pass this in from match container somehow
    //String GameID = MainActivity



    public static String[] viewMatchData() {

//Take In key puuid and match
        String APIKEY = "RGAPI-c9097aeb-6c07-43ab-b8d3-b28c2808b018";
        String PUUID = "HCuIWTxI-m_MifnTtvqiKmDJK1G925Poz9vn9FEV75ufFii8DJwlR53LLmw8979ytmdEPI9BxHpYyA";
        String MATCH = "NA1_4107774217";

//        MatchFeed.getMatchID();
//        String zoinks = MatchFeed.matID();//Retrieve the name
//        System.out.print(APIKEY);
//        System.out.print(zoinks);


        URL call1;
        String call1resp;


        StringBuilder sb = new StringBuilder();
        try {

            call1 = new URL ("https://americas.api.riotgames.com/tft/match/v1/matches/"+MATCH+"?api_key="+APIKEY);
            BufferedReader read = new BufferedReader( new InputStreamReader(call1.openStream()));
            while((call1resp = read.readLine()) != null)
                sb.append(call1resp + "\n");
            read.close();
        } catch (Exception e) {
            System.out.println(e);
        }



        String tester = sb.toString();
        //TOTAL SUBSTRING
        String total = tester.substring(tester.indexOf(""));
        //finding puuid in match data
        int x = 0;
        for (int index = total.indexOf(PUUID); index >= 0; index = total.indexOf(PUUID, index + 1)) {
            x = index;

        }

        //finding the end of players match data
        int y = 0;
        int first = 0;
        int index2;
        for (index2 = total.indexOf("companion"); index2 >= 0; index2 = total.indexOf("companion", index2 + 1)) {
            if(index2>x&&first==0) {
                ++first;
                y = index2;
            }
        }

        String playersData = tester.substring(x, y);


        int [] units = new int[700];
        int i = 0;
        for (int unitIndex = playersData.indexOf("TFT");unitIndex >= 0; unitIndex = playersData.indexOf("TFT", unitIndex + 1)) {
            units [i] = unitIndex;
            i++;
        }



        int j = 0;
        int [] unitE = new int[700];
        for (int unitEnd = playersData.indexOf("\",\"items\":");unitEnd >= 0; unitEnd = playersData.indexOf("\",\"items\":", unitEnd + 1)) {
            unitE [j] = unitEnd;
            j++;
        }



        //adding units to list
        String [] unitlist = new String [i+1];
        for(int z = 0; i>= z; z++) {
        	//Creates a list of units
        	unitlist [z] = playersData.substring(units[z], unitE[z]);
        }



           /* ArrayList<String> unitALL = new ArrayList<String>();
            for(int r = 0;  i>= r; r++) {
                unitALL.toArray(unitlist[r]);
            }
            return (unitALL);*/


        return(unitlist);
        //OUTPUT THE UNIT LIST

    }





    //Stock Stuff
    DrawerLayout drawerLayout;

    /////////////////////////////////////////////////////////////////////////////////////////
   //private static String z = MATCH;
/*
    public static void viewMatchData(){
        String a = MATCH;
        String z = a;
    }
    public static String matID(){
        return z;
    }*/
    ////////////////////////////////////////////////////////////////////////////////////////////

  /*
    public static String units()
    {
        return z;
    }
    String myValue = MatchDetails.viewMatchData();
    //Pass this in from match container somehow
    String GameID =
*/



/////////////////////////////////////////////////////////////////////////////////
/*

    //MatchID fetch
    String matID[];
    String [] listOfUnits = viewMatchData();

    public class addtolist(){

        int y = listOfUnits.length();
        ListView Ulist;
        for(int z = 0; y >= z; z++) {
            matID[z] = listOfUnits[z];
        }
    }*/
    String matID[] = { "Vi", "Zac", "Urgot", "Yuumi", "Jinx", "Tahmkench", "Jayce"};
    ListView Ulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_details);
//v
        Ulist = findViewById(R.id.list);
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item, matID);
        Ulist.setAdapter(arr);
//^


        drawerLayout = findViewById(R.id.drawer_layout);

        // temp functionality to go to match feed, to make merging everything in easier

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

    }

    public void ClickMenu(View view) {
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }






    public static void openDrawer(DrawerLayout drawerLayout){
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public static void closeDrawer(DrawerLayout drawerLayout){
        //Close drawer layout
        //Check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }







    public void ClickLogo(View view) {
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view) {
        //Redirect to home activity
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void ClickPopularBuilds(View view){
        //Redirect to Popular Builds activity
        MainActivity.redirectActivity(this, PopularBuilds.class);
    }
    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CommunityBuilds.class);
    }
    public void ClickCurrentCharacters(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }
    public void ClickItemBuilder(View view) {
        //Recreate the Item Builder activity
        MainActivity.redirectActivity(this,ItemBuilder.class);
    }
    public void ClickLogout(View view){
        //Logout and close app
        MainActivity.logout(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}
