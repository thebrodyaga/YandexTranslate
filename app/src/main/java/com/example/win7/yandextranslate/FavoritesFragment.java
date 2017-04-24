package com.example.win7.yandextranslate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.win7.yandextranslate.DB.DBHelper;
import com.example.win7.yandextranslate.dummy.DummyContent;
import com.example.win7.yandextranslate.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavoritesFragment extends Fragment implements View.OnClickListener {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    private ImageButton delete;
    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.titleToolbarFragmentFavorites);
        delete = (ImageButton) view.findViewById(R.id.imageButtonDelete);
        delete.setOnClickListener(this);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.listFavorites);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //recyclerView.setAdapter(new HistFrgAdapter(new DummyContent().getAll(), mListener));
        return view;
    }

    @Override
    public void onClick(View v) {//отчитска таблицы
        DBHelper.clear();//удаляет все
        recyclerView.setAdapter(new FavFrgAdapter(new DummyContent().getAll(), mListener));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Cycle", "History onStart");
        recyclerView.setAdapter(new FavFrgAdapter(new DummyContent().getFavorite(), mListener));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//обновление адаптера, при отображение фрагмента
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            recyclerView.setAdapter(new FavFrgAdapter(new DummyContent().getFavorite(), mListener));
    }

}
