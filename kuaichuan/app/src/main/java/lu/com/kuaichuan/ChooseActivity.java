package lu.com.kuaichuan;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class ChooseActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose);

        initToolBar();
        initNavigationDrawer();

    }

    private void initToolBar(){

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        toolbar.setTitle(R.string.disconnect);
        toolbar.setTitleTextColor(getResources().getColor(R.color.titleColor));
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item){
                int menuId = item.getItemId();
                switch (menuId){
                    case R.id.download:
                         Toast.makeText(ChooseActivity.this,"下载",Toast.LENGTH_SHORT).show();
                         break;
                    default:
                        break;

                }
                return true;
            }
        });
    }

    private void initNavigationDrawer(){

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);
        mNavigationView = (NavigationView)findViewById(R.id.design_navigation_view);

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                int menuItemId = item.getItemId();
                switch (menuItemId){
                    case R.id.user_name:
                        break;
                    case R.id.filter_size:
                        break;
                    default:
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }

        });
    }
}
