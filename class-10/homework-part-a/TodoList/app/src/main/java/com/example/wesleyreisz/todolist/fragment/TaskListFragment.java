package com.example.wesleyreisz.todolist.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wesleyreisz.todolist.R;
import com.example.wesleyreisz.todolist.TasksAdapter;
import com.example.wesleyreisz.todolist.model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wesleyreisz on 11/8/15.
 */
public class TaskListFragment extends Fragment {
    private Button btnCreate;
    private Button btnUpdate;

    private TasksAdapter adapter;
    private ArrayList<Task> arrayOfTasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        arrayOfTasks = new ArrayList<Task>();
        adapter = new TasksAdapter(getActivity(),arrayOfTasks);

        ListView listView = (ListView)view.findViewById(R.id.lv_task);
        listView.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null) {
                    for (ParseObject tasks : taskList) {

                        Task task = new Task();

                        task.setTaskId(tasks.getObjectId());
                        task.setName(tasks.getString("name"));

                        Log.d("name", task.getName());
                        adapter.add(task);
                        adapter.notifyDataSetChanged();

                        Log.d("Test", "Task Name: " + task.getName());
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = adapter.getItem(position);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UpdateFragment updateFragment = new UpdateFragment();
                updateFragment.setObjectId(task.getTaskId());
                fragmentTransaction.replace(R.id.fragmentContainer, updateFragment);
                fragmentTransaction.commit();
            }
        });


        btnCreate = (Button) view.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new CreateFragment());
                fragmentTransaction.commit();
            }
        });

        /*
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new UpdateFragment());
                fragmentTransaction.commit();
            }
        });
        */

        return view;
    }
}
