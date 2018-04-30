package com.rd.pageindicatorview.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rd.pageindicatorview.sample.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

	public static Fragment getInstance() {
		return new HomeFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_home, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		HomeAdapter adapter = new HomeAdapter();
		adapter.setData(createPageList());

		ViewPager pager = view.findViewById(R.id.viewPager2);
		pager.setAdapter(adapter);
	}

	@NonNull
	private List<View> createPageList() {
		List<View> pageList = new ArrayList<>();
		pageList.add(createPageView(R.color.green_50));
		pageList.add(createPageView(R.color.green_100));
		pageList.add(createPageView(R.color.green_200));
		pageList.add(createPageView(R.color.green_300));

		return pageList;
	}

	@NonNull
	private View createPageView(int color) {
		View view = new View(getActivity());
		view.setBackgroundColor(getResources().getColor(color));

		return view;
	}
}
