package steam.appjam.sopt.com.myapplication.board;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import steam.appjam.sopt.com.myapplication.R;


/**
 * Created by user on 2016-06-30.
 */
public class BoardItem {
    String tv_id;
    String tv_title;
    String tv_number;
    String tv_name;
    String tv_date;
    String tv_type;
    String tv_region;

    public BoardItem(String tv_id,String tv_title, String tv_number, String tv_name, String tv_date,String tv_type, String tv_region)
    {
        this.tv_id = tv_id;
        this.tv_title = tv_title;
        this.tv_number = tv_number;
        this.tv_name = tv_name;
        this.tv_date = tv_date;
        this.tv_type = tv_type;
        this.tv_region = tv_region;
    }

    public String getTv_id() {
        return tv_id;
    }

    public String getTv_title() {
        return tv_title;
    }

    public String getTv_number() {
        return tv_number;
    }

    public String getTv_name() {
        return tv_name;
    }

    public String getTv_date() {
        return tv_date;
    }

    public String getTv_type() {
        return tv_type;
    }

    public String getTv_region() {
        return tv_region;
    }

    public void setTv_title(String tv_title) {
        this.tv_title = tv_title;
    }

    public void setTv_number(String tv_number) {
        this.tv_number = tv_number;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }

    public void setTv_date(String tv_date) {
        this.tv_date = tv_date;
    }

    public void setTv_type(String tv_type) {
        this.tv_type = tv_type;
    }

    public void setTv_region(String tv_region) {
        this.tv_region = tv_region;
    }

    public static class BoardRegister extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_board_register);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
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
//            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

//            //noinspection SimplifiableIfStatement
//            if (id == R.id.action_settings) {
//                return true;
//            }

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

            } else if (id == R.id.nav_board) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }
}
