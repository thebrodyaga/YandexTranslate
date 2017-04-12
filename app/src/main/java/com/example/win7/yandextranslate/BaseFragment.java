package com.example.win7.yandextranslate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class BaseFragment extends Fragment {
    EditText inText;
    TextView outText;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        inText = (EditText) view.findViewById(R.id.inText);
        outText = (TextView) view.findViewById(R.id.outText);
        MyTextWatcher inputTextWatcher = new MyTextWatcher(inText, outText);
        inText.addTextChangedListener(inputTextWatcher);
        return view;
    }
}
