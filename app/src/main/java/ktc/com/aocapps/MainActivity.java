package ktc.com.aocapps;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView mGrid;
    private ArrayList<ResolveInfo> mData;
    private aocAppAdapter mAdapter;
    private LoadAppTask mTask;
    private PackageManager mPackageManager;
    private final static String[] PACKAGE_FILTER_LIST = {
            "com.android.contacts",
            "com.android.camera2",
            "com.android.dummyactivity",
            "ktc.factorymenu.ui",
            "com.mstar.tvsetting",
            "com.android.mslauncher",
            "mstar.factorymenu.ui",

    };
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        mPackageManager = getApplicationContext().getPackageManager();
        initGridView();
        Log.w("dz", "onCreate: "+getTopActivity(this) );
    }
    public static String getTopActivity(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("dz", "pkg:"+cn.getPackageName());//包名
        Log.d("dz", "cls:"+cn.getShortClassName());//包名加类名
        return cn.getClassName();
    }
    private void initGridView () {
        mGrid = (GridView) findViewById(R.id.grid_apps);
        mData = new ArrayList<ResolveInfo>();
        mAdapter = new aocAppAdapter(this, mData);
        mGrid.setAdapter(mAdapter);
        update(false);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Log.w("dz", "setOnItemClickListener: " );
                    jumpToApp(mData.get(i).activityInfo.packageName,mData.get(i).activityInfo.name);
                }catch (Exception e){

                }

            }
        });
    }
    void update (boolean isDataSetNotChange) {
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new LoadAppTask();
        mTask.execute(isDataSetNotChange);
    }
    class LoadAppTask extends AsyncTask<Boolean, Void, List<ResolveInfo>> {

        @Override
        protected List<ResolveInfo> doInBackground (Boolean... voids) {
            List<ResolveInfo> list;
            Log.w("dz", "doInBackground: " );
            if (!voids[0]) {
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                list = mPackageManager.queryIntentActivities(intent, 0);
                list = filter(list);
            } else {
                list = mData;
            }
            return list;
        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute (List<ResolveInfo> beans) {
            Log.w("dz", "onPostExecute: " );
            mData.clear();
            mData.addAll(beans);
            mAdapter.notifyDataSetChanged();

        }
    }
    private List<ResolveInfo> filter (List<ResolveInfo> sourceData) {

        List<ResolveInfo> filterData = new ArrayList<ResolveInfo>();;

        filterData = new ArrayList<ResolveInfo>();

        List<String> filterList = Arrays.asList(PACKAGE_FILTER_LIST);
        for (ResolveInfo info : sourceData) {
            if (filterList.contains(info.activityInfo.packageName)) {

                continue;
            }
            filterData.add(info);
        }

        return filterData;
    }

    private void jumpToApp (String pkg,String  activityName) {
        Log.w("dz", "jumpToApp: "+pkg+activityName );
         intent= new Intent();
        ComponentName cn = new ComponentName(pkg,activityName);
        intent.setComponent(cn);
        startActivity(intent);

    }
}
