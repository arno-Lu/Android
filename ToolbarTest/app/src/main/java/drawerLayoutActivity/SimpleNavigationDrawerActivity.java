package drawerLayoutActivity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.mechrevo.toolbartest.R;

/**
 * Created by mechrevo on 2016/4/15.
 */
public class SimpleNavigationDrawerActivity extends AppCompatActivity{

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_simple_navigation_drawer);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);
        mNavigationView =(NavigationView)findViewById(R.id.navigation_view);

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int meenuItemId = item.getItemId();
                switch (meenuItemId) {
                    case R.id.subitem_01:
                        Toast.makeText(SimpleNavigationDrawerActivity.this, "sub item 01", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.subitem_02:
                        Toast.makeText(SimpleNavigationDrawerActivity.this, "sub item 02", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.subitem_03:
                        Toast.makeText(SimpleNavigationDrawerActivity.this, "sub item 03", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.subitem_04:
                        Toast.makeText(SimpleNavigationDrawerActivity.this, "sub item 04", Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }
        });
    }
}
