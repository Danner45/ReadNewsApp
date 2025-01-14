package project.an.readnewsapp.Service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    public void setQuery(String query){
        queryLiveData.setValue(query);
    }

    public LiveData<String> getQuery(){
        return queryLiveData;
    }
}
