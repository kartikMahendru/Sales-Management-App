package project.avishkar.salesmanagement;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.io.File;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;

public class manager_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText itemName,q;
    private DatabaseReference databaseRef;
    private SwipeMenuListView listView;
    private ArrayList<InventoryItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*

        databaseRef = FirebaseDatabase.getInstance().getReference(ref[0]);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // dialog box to add item on dashboard
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(manager_main.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_box_add_item_inventory, null);

                Button ok = (Button) mView.findViewById(R.id.ok);
                itemName = mView.findViewById(R.id.item_name);
                q = mView.findViewById(R.id.quantity);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String item = itemName.getText().toString();
                        int quant = Integer.parseInt(q.getText().toString());
                        InventoryItem it = new InventoryItem(item, quant);

                        databaseRef.child(tmp).child("Inventory").child(item).setValue(it);
                        dialog.dismiss();

                        listView=findViewById(R.id.items_list);
                        final ArrayList<InventoryItem> data = new ArrayList<>();
                        databaseRef.child(tmp).child("Inventory")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            InventoryItem it1 = snapshot.getValue(InventoryItem.class);
                                            data.add(it1);
                                        }
                                        CustomAdapter mAdapter = new CustomAdapter(getApplicationContext(),data);
                                        listView.setAdapter(mAdapter);

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                        Snackbar snackbar = Snackbar.make(view , "Item added successfully !", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                });

            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        listView=findViewById(R.id.items_list);

        SwipeMenuCreator creator= new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // create "open" item

                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("Open");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                // create "delete" item

                SwipeMenuItem deleteItem=new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9,0xC9,0xCE)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.RED);
                menu.addMenuItem(deleteItem);
            }

            public int dp2px(int dp)
            {
                return (int) (dp * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
            }
        };

        listView.setMenuCreator(creator);
        // for creating a bounce effect
        listView.setCloseInterpolator(new BounceInterpolator());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            // exit dialog box
            new FancyAlertDialog.Builder(this)
                    .setTitle("Warning!!!")
                    .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                    .setMessage("Do you really want to Exit ?")
                    .setNegativeBtnText("No")
                    .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                    .setPositiveBtnText("Yes")
                    .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                    .setAnimation(Animation.POP)
                    .isCancellable(true)
                    .setIcon(R.drawable.ic_error_outline_black_24dp, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            manager_main.super.onBackPressed();
                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {

                        }
                    })
                    .build();
            // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manager_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.dashboard) {

        }
        else if (id == R.id.my_account) {
            //show the manager's account
            Intent intent = new Intent(manager_main.this,AccountManager.class);
            startActivity(intent);

        } else if (id == R.id.my_team) {

        } else if (id == R.id.statistics) {

        } else if (id == R.id.nav_share) {
            // Share app with others
            ApplicationInfo api = getApplicationContext().getApplicationInfo();
            String apkpath = api.sourceDir;
            Intent share_intent = new Intent(Intent.ACTION_SEND);
            share_intent.setType("application/vnd.android.package-archive");
            share_intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(share_intent, "Share app using"));
        } else if (id == R.id.nav_send) {
            // Share invite-code with salespersons
            String shareBody = "invite-code";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite code -");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Send invite-code using"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
