package com.example.win7.yandextranslate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class BaseFragment extends Fragment implements View.OnClickListener {
    EditText inText;
    TextView outText;
    TextView langIn;
    TextView langOut;

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
        langIn = (TextView) view.findViewById(R.id.langIn);
        langOut = (TextView) view.findViewById(R.id.langOut);
        MyTextWatcher inputTextWatcher = new MyTextWatcher(inText, outText);
        inText.addTextChangedListener(inputTextWatcher);

        langIn.setOnClickListener(this);
        langOut.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.langIn:
                Log.d("MyLog","klikaetIn");
                startActivity(new Intent(getActivity(), InLangs.class));
                break;
            case R.id.langOut:
                Log.d("MyLog","klikaetOut");
                startActivity(new Intent(getActivity(), OutLangs.class));
                break;
        }
    }
}
