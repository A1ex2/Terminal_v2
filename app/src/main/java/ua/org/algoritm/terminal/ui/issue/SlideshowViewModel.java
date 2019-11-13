package ua.org.algoritm.terminal.ui.issue;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Этот раздел в разработке");
    }

    public LiveData<String> getText() {
        return mText;
    }
}