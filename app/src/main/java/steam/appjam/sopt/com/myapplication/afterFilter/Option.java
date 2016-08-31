package steam.appjam.sopt.com.myapplication.afterFilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.board.BoardView;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;

public class Option extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<ListItem> item = new ArrayList<ListItem>();
        ListView listview = (ListView)findViewById(R.id.ListView1);


//        item.add(new ListItem(null, "a", "b", "c", "d", R.drawable.img1));
//        item.add(new ListItem(null,"a1","b2","c1","d1", R.drawable.img1));
//        item.add(new ListItem(null,"a2","b3","c2","d2", R.drawable.img1));
//        item.add(new ListItem(null,"a3","b4","c3","d3", R.drawable.img1));
//        item.add(new ListItem(null, "a4", "b5", "c4", "d4", R.drawable.img1));
//        item.add(new ListItem(null, "a", "b", "c", "d", R.drawable.img1));
//        item.add(new ListItem(null,"a1","b2","c1","d1", R.drawable.img1));
//        item.add(new ListItem(null,"a2","b3","c2","d2", R.drawable.img1));
//        item.add(new ListItem(null,"a3","b4","c3","d3", R.drawable.img1));
//        item.add(new ListItem(null,"a4","b5","c4","d4", R.drawable.img1));

        FilterAdapter adapter = new FilterAdapter(getApplicationContext(),item);
        listview.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            // Handle the camera action
        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(getApplicationContext(), FavoriteSpace.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_board) {
            Intent intent = new Intent(getApplicationContext(), BoardView.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
