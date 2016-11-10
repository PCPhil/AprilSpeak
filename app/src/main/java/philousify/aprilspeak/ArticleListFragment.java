package philousify.aprilspeak;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Philip on 11/7/2016.
 */

public class ArticleListFragment extends ListFragment {
    String[] array = {"s1","s2","s2"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> s = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,array);
        setListAdapter(s);

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
