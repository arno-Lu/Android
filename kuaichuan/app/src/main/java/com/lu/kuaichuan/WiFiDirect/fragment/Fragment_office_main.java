package com.lu.kuaichuan.wifidirect.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lu.kuaichuan.wifidirect.R;

/**
 * Created by Lu on 2016/5/24.
 */
public class Fragment_office_main extends Fragment {


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View mainView = inflater.inflate(R.layout.fragment_office_main,container,false);


        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        View doc = getActivity().findViewById(R.id.localefile_doc);
        View ppt = getActivity().findViewById(R.id.localefile_ppt);
        View xls = getActivity().findViewById(R.id.localefile_xls);
        View pdf = getActivity().findViewById(R.id.localefile_pdf);
        View txt = getActivity().findViewById(R.id.localefile_txt);

        doc.setOnClickListener(new ItemClickListener());
        ppt.setOnClickListener(new ItemClickListener());
        xls.setOnClickListener(new ItemClickListener());
        pdf.setOnClickListener(new ItemClickListener());
        txt.setOnClickListener(new ItemClickListener());


    }


    class ItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.localefile_doc:
                    //如果是全局事务，则不能重复提交
                    FragmentManager fragmentManager_doc =getFragmentManager();
                    FragmentTransaction transaction_doc = fragmentManager_doc.beginTransaction();

                    Bundle bundle_doc = new Bundle();
                    bundle_doc.putString("Type","DOC");
                    Fragment_office_make fragment_office_doc = new Fragment_office_make();
                    fragment_office_doc.setArguments(bundle_doc);

                    transaction_doc.replace(R.id.main_office_id,fragment_office_doc);
                    transaction_doc.addToBackStack(null);
                    transaction_doc.commit();

                    break;

                case R.id.localefile_ppt:

                    FragmentManager fragmentManager_ppt =getFragmentManager();
                    FragmentTransaction transaction_ppt = fragmentManager_ppt.beginTransaction();

                    Bundle bundle_ppt = new Bundle();
                    bundle_ppt.putString("Type","PPT");
                    Fragment_office_make fragment_office_ppt = new Fragment_office_make();
                    fragment_office_ppt.setArguments(bundle_ppt);

                    transaction_ppt.replace(R.id.main_office_id,fragment_office_ppt);
                    transaction_ppt.addToBackStack(null);
                    transaction_ppt.commit();

                    break;

                case R.id.localefile_xls:

                    FragmentManager fragmentManager_xls =getFragmentManager();
                    FragmentTransaction transaction_xls = fragmentManager_xls.beginTransaction();

                    Bundle bundle_xls = new Bundle();
                    bundle_xls.putString("Type","XLS");
                    Fragment_office_make fragment_office_xls = new Fragment_office_make();
                    fragment_office_xls.setArguments(bundle_xls);

                    transaction_xls.replace(R.id.main_office_id,fragment_office_xls);
                    transaction_xls.addToBackStack(null);
                    transaction_xls.commit();

                    break;

                case R.id.localefile_pdf:

                    FragmentManager fragmentManager_pdf =getFragmentManager();
                    FragmentTransaction transaction_pdf = fragmentManager_pdf.beginTransaction();

                    Bundle bundle_pdf = new Bundle();
                    bundle_pdf.putString("Type","PDF");
                    Fragment_office_make fragment_office_pdf = new Fragment_office_make();
                    fragment_office_pdf.setArguments(bundle_pdf);

                    transaction_pdf.replace(R.id.main_office_id,fragment_office_pdf);
                    transaction_pdf.addToBackStack(null);
                    transaction_pdf.commit();

                    break;

                case R.id.localefile_txt:

                    FragmentManager fragmentManager_txt =getFragmentManager();
                    FragmentTransaction transaction_txt = fragmentManager_txt.beginTransaction();

                    Bundle bundle_txt = new Bundle();
                    bundle_txt.putString("Type","TXT");
                    Fragment_office_make fragment_office_txt = new Fragment_office_make();
                    fragment_office_txt.setArguments(bundle_txt);

                    transaction_txt.replace(R.id.main_office_id,fragment_office_txt);
                    transaction_txt.addToBackStack(null);
                    transaction_txt.commit();

                    break;
                default:
                    break;
            }

        }
    }

}