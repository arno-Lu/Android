package Toolbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.mechrevo.toolbartest.R;

/**
 * Created by mechrevo on 2016/4/12.
 */
public class ToolBarActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tool_bar);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);   //导航图标
        toolbar.setLogo(R.mipmap.ic_launcher);    //logo
        toolbar.setTitle("Title");          //主标题
        toolbar.setSubtitle("Subtitle");    //子标题

        toolbar.inflateMenu(R.menu.base_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item){
                int menuId = item.getItemId();
                switch (menuId){
                    case R.id.action_search:
                        Toast.makeText(ToolBarActivity.this,R.string.menu_search,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_notification:
                        Toast.makeText(ToolBarActivity.this,R.string.menu_notifications,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_item1:
                        Toast.makeText(ToolBarActivity.this,R.string.item_01,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_item2:
                        Toast.makeText(ToolBarActivity.this,R.string.item_02,Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }

        });
    }
}
