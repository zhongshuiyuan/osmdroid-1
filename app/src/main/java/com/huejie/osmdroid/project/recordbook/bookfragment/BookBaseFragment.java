package com.huejie.osmdroid.project.recordbook.bookfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huejie.osmdroid.model.books.BookSimple;


public abstract class BookBaseFragment extends Fragment {


    public BookBaseFragment() {

    }

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public abstract void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    public abstract BookSimple getBookContent();



}
