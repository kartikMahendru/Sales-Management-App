package project.avishkar.salesmanagement;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class myTeam extends AppCompatActivity {

    private SwipeMenuListView listView;
    private MyTeamAdapter myTeamAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        listView=findViewById(R.id.teamListView);
        ArrayList<String> list=new ArrayList<>();
        list.add("Utkarsh garg");
        list.add("Mehul Garg");
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create Details item
                SwipeMenuItem detailsItem = new SwipeMenuItem(
                        getApplicationContext());
                detailsItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                detailsItem.setWidth(dp2px(90));
                detailsItem.setTitle("Details");
                detailsItem.setTitleSize(18);
                detailsItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(detailsItem);

                // create delete item
                SwipeMenuItem messageItem = new SwipeMenuItem(
                        getApplicationContext());
                messageItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                messageItem.setWidth(dp2px(90));
                messageItem.setIcon(R.drawable.ic_message);
                menu.addMenuItem(messageItem);
            }

            public  int dp2px(float dips)
            {
                return (int) (dips * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // Details
                        Toast.makeText(getApplicationContext(),"Details Clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // Message
                        Toast.makeText(getApplicationContext(),"Message Clicked",Toast.LENGTH_SHORT).show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setCloseInterpolator(new BounceInterpolator());

        myTeamAdapter= new MyTeamAdapter(this,list);
        listView.setAdapter(myTeamAdapter);
    }
}
