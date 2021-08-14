package com.mirrordust.telecomlocate.fragment;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.mirrordust.telecomlocate.R;
import com.mirrordust.telecomlocate.activity.PredictionActivity;
import com.mirrordust.telecomlocate.activity.SampleDetailActivity;
import com.mirrordust.telecomlocate.entity.BaseStation;
import com.mirrordust.telecomlocate.entity.Sample;
import com.mirrordust.telecomlocate.livedata.MRdata;
import com.mirrordust.telecomlocate.model.DataHelper;
import com.srplab.www.starcore.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PredFragment extends Fragment {
    //预测的经纬坐标
    private List<Double> x;
    private List<Double> y;
    //实际的经纬坐标
    private double actualPosX;
    private double actualPosY;
    //lv中data
    private List<String> data;
    private PredViewModel mViewModel;
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private static final String[] strs = new String[]{
            "first", "second", "third", "fourth", "fifth"
    };

    public PredFragment() {
        x=new ArrayList<>();
        y=new ArrayList<>();
        data=new ArrayList<>();
    }

    public static PredFragment newInstance() {
        return new PredFragment();
    }

    public StarSrvGroupClass srvGroup;
    public static SampleDetailActivity Host;

    public void click(View view) {

        File destDir = new File("/data/data/" + getActivity().getPackageName() + "/files");
        if (!destDir.exists())
            destDir.mkdirs();
        java.io.File python2_7_libFile = new java.io.File("/data/data/" + getActivity().getPackageName() + "/files/python3.7.zip");

        if (!python2_7_libFile.exists()) {
            try {
                copyFile(getActivity(), "python3.7.zip", null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            copyFile(getActivity(), "_struct.cpython-37m.so", null);
            copyFile(getActivity(), "binascii.cpython-37m.so", null);
            copyFile(getActivity(), "zlib.cpython-37m.so", null);
        } catch (Exception e) {
            System.out.println(e);
        }

        String pystring = null;

        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream dataSource = assetManager.open("prediction.py");
            int size = dataSource.available();
            byte[] buffer = new byte[size];
            dataSource.read(buffer);
            dataSource.close();
            pystring = new String(buffer);
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            //--load python34 core library first;
            System.load(getActivity().getApplicationInfo().nativeLibraryDir + "/libpython3.7m.so");
        } catch (UnsatisfiedLinkError ex) {
            System.out.println(ex.toString());
        }

        /*----init starcore----*/

        StarCoreFactoryPath.StarCoreCoreLibraryPath = getActivity().getApplicationInfo().nativeLibraryDir;

        StarCoreFactoryPath.StarCoreShareLibraryPath = getActivity().getApplicationInfo().nativeLibraryDir;

        StarCoreFactoryPath.StarCoreOperationPath = "/data/data/" + getActivity().getPackageName() + "/files";

        final StarCoreFactory starcore = StarCoreFactory.GetFactory();
        Integer s = new Random().nextInt(100);
        StarServiceClass Service = starcore._InitSimple("MathTest" + s, "123", 0, 0);
        srvGroup = (StarSrvGroupClass) Service._Get("_ServiceGroup");
        Service._CheckPassword(false);
        starcore._SRPLock();
        srvGroup = starcore._GetSrvGroup(0);
        Service = srvGroup._GetService("test", "123");
        if (Service == null) {
            Service = starcore._InitSimple("test", "123", 0, 0);
        } else {
            Service._CheckPassword(false);
        }
        Service._CheckPassword(false);


//        StarServiceClass  Service = starcore._InitSimple("test", "123", 0, 0);
//        SrvGroup = (StarSrvGroupClass) Service._Get("_ServiceGroup");
//        Service._CheckPassword(false);

        /*----run python code----*/
        srvGroup._InitRaw("python37", Service);
        StarObjectClass python = Service._ImportRawContext("python", "", false, "");
        // 设置Python模块加载路径
        python._Call("import", "sys");
        StarObjectClass pythonSys = python._GetObject("sys");
        StarObjectClass pythonPath = (StarObjectClass) pythonSys._Get("path");
        pythonPath._Call("insert", 0, "/data/data/" + getActivity().getPackageName() + "/files/python3.7.zip");
        pythonPath._Call("insert", 0, getActivity().getApplicationInfo().nativeLibraryDir);
        pythonPath._Call("insert", 0, "/data/data/" + getActivity().getPackageName() + "/files");
        //
        starcore._SRPUnLock();


        /*----run python code----*/
//        srvGroup._InitRaw("python37", Service);
//        StarObjectClass python = Service._ImportRawContext("python", "", false, "");
//        python._Call("import", "sys");
//        StarObjectClass pythonSys = python._GetObject("sys");
//        StarObjectClass pythonPath = (StarObjectClass) pythonSys._Get("path");
//        pythonPath._Call("insert", 0, "/data/data/" + getActivity().getPackageName() + "/files/python3.7.zip");
//        pythonPath._Call("insert", 0, getActivity().getApplicationInfo().nativeLibraryDir);
//        pythonPath._Call("insert", 0, "/data/data/" + getActivity().getPackageName() + "/files");
//        python._Call("execute", pystring);
//
        Object object = python._Call("add", 11, 22);
//
        Toast.makeText(getActivity(), object.toString(), Toast.LENGTH_LONG).show();
//        String CorePath = "/data/data/" + getActivity().getPackageName() + "/files";

//        python._Set("JavaClass", CallBackClass.class);
//        Service._DoFile("python", CorePath + "/test_calljava.py", "");
    }

    private void copyFile(Activity c, String Name, String desPath) throws IOException {

        File outfile = null;
        if (desPath != null)
            outfile = new File("/data/data/" + getActivity().getPackageName() + "/files/" + desPath + Name);
        else
            outfile = new File("/data/data/" + getActivity().getPackageName() + "/files/" + Name);

        outfile.createNewFile();
        FileOutputStream out = new FileOutputStream(outfile);

        byte[] buffer = new byte[1024];
        InputStream in;
        int readLen = 0;

        if (desPath != null)
            in = c.getAssets().open(desPath + Name);
        else
            in = c.getAssets().open(Name);
        while ((readLen = in.read(buffer)) != -1) {
            out.write(buffer, 0, readLen);
        }

        out.flush();
        in.close();
        out.close();

    }

    // 初始化Python环境
    public void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(getContext()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pred, container, false);
        //异步调用python预测算法
        PredictionAsyncTask predictionAsyncTask=new PredictionAsyncTask();
        predictionAsyncTask.execute();
        lv = (ListView) view.findViewById(R.id.lv);
        //为ListView设置Adapter来绑定数据
        arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, updateData());
        lv.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mViewModel = ViewModelProviders.of(getActivity()).get(PredViewModel.class);
//        // TODO: Use the ViewModel
//        //final PredViewModel PredViewModel = ViewModelProviders.of(getActivity()).get(PredViewModel.class);
//
//        //通过observe方法观察ViewModel中字段数据的变化，并在变化时，得到通知
//        mViewModel.getMRdata().observe((LifecycleOwner) this, new Observer<MRdata>()
//        {
//            @Override
//            public void onChanged(@Nullable MRdata mRdata)
//            {
//
//            }
//        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private List<String> updateData() {
        if(data.isEmpty()) {
            for (int i = 0; i < 1; i++) {
                data.add("random forest prediction:");
                data.add("predicted position:predicting...");
                data.add("actual position:"+actualPosX+";"+actualPosY);
            }
        }
        else{
            data.clear();
            for (int i = 0; i < 1; i++) {
                data.add("random forest prediction:");
                data.add("predicted position:" + x.get(i) + ";" + y.get(i));
                data.add("actual position:"+actualPosX+";"+actualPosY);
            }
        }
        return data;
    }

    public class PredictionAsyncTask extends AsyncTask<Void, Void, List<Double>> {
        JSONObject json;
        Python py;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SampleDetailActivity parentActivity = (SampleDetailActivity) getActivity();
            String mID = parentActivity.getSampleId();
            Sample sample = DataHelper.getSample(parentActivity.getRealm(), mID);
            BaseStation mbs = sample.getMBS();
            List<BaseStation> bslist = sample.getBSList();
            actualPosX=sample.getLatLng().getLongitude();
            actualPosY=sample.getLatLng().getLatitude();

            HashMap<String, String> parameterMap = new HashMap<>();
            initPython();
            py = Python.getInstance();
            parameterMap.put("ss_1", String.valueOf(mbs.getDbm()));
            json = new JSONObject();
            try {
                json.put("ss_1", String.valueOf(mbs.getDbm()));
                json.put("lac_1",String.valueOf(mbs.getLac()));
                json.put("cid_1",String.valueOf(mbs.getCid()));
                json.put("mnc_1",String.valueOf(mbs.getLon()));
                json.put("lon",String.valueOf(mbs.getLon()));
                json.put("lat",String.valueOf(mbs.getLat()));
                if(!bslist.isEmpty()) {
                    for(int i=2;i<bslist.size()+2;i++){
                        json.put("ss_"+i,String.valueOf(bslist.get(i-2).getDbm()));
                        json.put("lac_"+i,String.valueOf(bslist.get(i-2).getLac()));
                        json.put("cid_"+i,String.valueOf(bslist.get(i-2).getCid()));
                        json.put("mnc_"+i,String.valueOf(bslist.get(i-2).getMnc()));
                        json.put("lon_"+i,String.valueOf(bslist.get(i-2).getLon()));
                        json.put("lat_"+i,String.valueOf(bslist.get(i-2).getLat()));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected List<Double> doInBackground(Void... voids) {


            JSONArray jsonarray = new JSONArray();
            jsonarray.put(json);
            String parameterJson = jsonarray.toString();
            PyObject obj1 = py.getModule("prediction").callAttr("prediction_rf", new Kwarg("parameterJson", parameterJson));
            List<PyObject> result = obj1.asList();
            Double x=result.get(0).toDouble();
            Double y=result.get(1).toDouble();
            ArrayList<Double> xy=new ArrayList<>();
            xy.add(x);
            xy.add(y);
            return xy;
        }

        @Override
        protected void onPostExecute(List<Double> doubles) {
            super.onPostExecute(doubles);
            x.add(doubles.get(0));
            y.add(doubles.get(1));
            //更新数据及UI
            updateData();
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
