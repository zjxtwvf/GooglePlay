package com.example.googlepaly.fragment;

import java.util.HashMap;


public class FragmentFactory {
	private static HashMap<Integer, BaseFragment> fragmentList = new HashMap<Integer, BaseFragment>();
	
    public static BaseFragment creatFragment(int position){
    	BaseFragment fragment = null;
    	if(fragmentList.get(position) != null){
    		return fragmentList.get(position);
    	}
    	switch(position){
	    	case 0:
	    		fragment = new HomeFragment();
	    		break;
	    	case 1:
	    		fragment = new AppFragment();
	    		break;
	    	case 2:
	    		fragment = new GameFragment();
	    		break;
	    	case 3:
	    		fragment = new SubjectFragment();
	    		break;
	    	case 4:
	    		fragment = new RecommentFragment();
	    		break;
	    	case 5:
	    		fragment = new CategoryFragment();
	    		break;
	    	case 6:
	    		fragment = new HotFragment();
	    		break;	    		

    	}
    	
    	fragmentList.put(position, fragment);
    	return fragment;
    }
}
