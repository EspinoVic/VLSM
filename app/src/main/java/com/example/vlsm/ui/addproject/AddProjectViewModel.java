package com.example.vlsm.ui.addproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vlsm.data.model.Project;
import com.example.vlsm.data.model.SubRed;

import java.util.ArrayList;

public class AddProjectViewModel  extends ViewModel {

    private MutableLiveData<Project> project;
/*
    private MutableLiveData<ArrayList<SubRed>> projectNodeList;
*/


    public MutableLiveData<Project> getProject() {
        if(project==null){
            project = new MutableLiveData<>();
        }
        return project;
    }

/*    public void setProject(Project project) {
        this.project.setValue(project);
    }*/

    /*
    public LiveData<ArrayList<SubRed>> getProjectNodes(){
        return projectNodeList;
    }
    public void setProjectNodeList(ArrayList<SubRed> list){
        this.projectNodeList.setValue(list);
    }
*/


}
