package com.mirrordust.telecomlocate.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mirrordust.telecomlocate.R;
import com.mirrordust.telecomlocate.adapter.SampleAdapter;
import com.mirrordust.telecomlocate.interf.CollectionContract;
import com.mirrordust.telecomlocate.interf.OnSamplesUpdateListener;
import com.mirrordust.telecomlocate.service.SampleService;
import com.mirrordust.telecomlocate.entity.Sample;

import io.realm.Realm;
import io.realm.RealmResults;

public class CollectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CollectionActivity";

    private CollectionContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private SampleAdapter mAdapter;

    private TextView mTextView;

    private FloatingActionButton fab;






    private static final int REQUEST_PERMISSION = 123;
    private boolean mBound = false;
    private boolean recording = false;




    private Realm realm;
    private SampleService mSampleService;
    private String mode;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SampleService.LocalBinder binder = (SampleService.LocalBinder) iBinder;
            mSampleService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    private boolean isPermissionGranted() {
        return !(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    private void checkPermissionUser1() {
        if (!isPermissionGranted()) {
            stopRecording();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            startRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                stopRecording();
                Toast.makeText(this, "Error permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        initMainView();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recording) {
                    checkPermissionUser1();
                } else {
                    stopRecording();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(this, SampleService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        testmethod();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkGPSStatus();
    }

    private void checkGPSStatus() {
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS is not enabled, open it now?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayMainView();
        if (recording) {
            setTitle("TelecomLocate" + "(recording...)");
        } else {
            setTitle("TelecomLocate" + "(stopped)");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        mRecyclerView.setAdapter(null);
        realm.close();
    }

    private void startRecording() {
        final String[] modes = getResources().getStringArray(R.array.travel_mode);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Mode")
                .setItems(modes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mode = modes[which];
                        if (!mSampleService.isRecordManagerInitialized()) {
                            mSampleService.setRecordManager(getApplicationContext());
                        }
                        if (mSampleService.getOnSamplesUpdateListener() == null) {
                            mSampleService.setOnSamplesUpdateListener(new OnSamplesUpdateListener() {
                                @Override
                                public void onSamplesUpdate() {
                                    mAdapter.addSample();
                                    mRecyclerView.scrollToPosition(0);
                                    //initListRecords();
                                }
                            });
                        }
                        mSampleService.setMode(mode);
                        mSampleService.startCollecting();
                        recording = true;
                        setTitle("TelecomLocate" + "(recording...)");
                        fab.setImageDrawable(ContextCompat.getDrawable(
                                getApplicationContext(), R.drawable.ic_fab_stop));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void stopRecording() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stop recording?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSampleService.stopCollecting();
                        recording = false;
                        setTitle("TelecomLocate" + "(stopped)");
                        fab.setImageDrawable(ContextCompat.getDrawable(
                                getApplicationContext(), R.drawable.ic_fab_start));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
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
        getMenuInflater().inflate(R.menu.new_sample_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveNewData();
        } else if (id == R.id.action_discard) {
            discardNewData();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle sample_detail_navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_test:
                break;
            case R.id.nav_data:
                break;
            case R.id.nav_prediction:
                Toast.makeText(this, "Not available now", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_collection_setting:
                break;
            case R.id.nav_prediction_setting:
                Toast.makeText(this, "Not available now", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_about:
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void saveNewData() {
        //TODO: save new data
    }

    private void discardNewData() {
        //TODO: discard new data
    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    private float convertPitoSp(float px) {
        return px / getResources().getDisplayMetrics().scaledDensity;
    }

    private void initMainView() {
        mTextView = (TextView) findViewById(R.id.placeholder_text_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.new_sample_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SampleAdapter(this, getNewSamples());
        mRecyclerView.setAdapter(mAdapter);
        displayMainView();
    }

    private void displayMainView() {
        if (hasNewSample()) {
            switchView(mRecyclerView);
        } else {
            switchView(mTextView);
        }
    }

    private RealmResults<Sample> getNewSamples() {
        return realm.where(Sample.class)
                .equalTo("index", 0)
                .findAll();
    }

    private void switchView(View v) {
        if (v instanceof RecyclerView) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.GONE);
        } else if (v instanceof TextView) {
            mTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private boolean hasNewSample() {
//        return getNewSamples().size() != 0;
        return true;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void testmethod() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Sample> samples = realm.where(Sample.class).findAll();
        Log.e(TAG, "RealmResults<Sample>长度: "+samples.size());
    }
}
