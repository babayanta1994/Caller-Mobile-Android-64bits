package ru.true_ip.trueip.app.main_screen.photo_fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.app.main_screen.photo_details_activity.PhotoDetailsActivity;
import ru.true_ip.trueip.app.main_screen.photo_fragment.adapters.PhotoAdapter;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.databinding.FragmentPhotoBinding;
import ru.true_ip.trueip.models.PhotoModel;
import ru.true_ip.trueip.models.PhotoTestModel;
import ru.true_ip.trueip.utils.Constants;


/**
 * Created by user on 11-Sep-17.
 */

public class PhotoPresenter extends BasePresenter<BaseContract> {

    private static final String TAG = PhotoPresenter.class.getSimpleName();
    public PhotoAdapter photoAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Fragment fragment;

    public void setFragment(Fragment f) { fragment = f; }

    private List<PhotoTestModel> createPhotoTestModels(int maxNum) {
        List<PhotoTestModel> photos = new ArrayList<>();
        for (int i = 0; i < maxNum; i++) {
            photos.add(new PhotoTestModel());
        }
        return photos;
    }

    private List<PhotoModel> createPhotoModels() {
        List<PhotoModel> photos = new ArrayList<>();

        List<File> imageFiles = getSnapshots();

        for(File file : imageFiles) {
            photos.add(new PhotoModel(file.getAbsolutePath(), file.lastModified()));
        }

        return photos;
    }

    public void createPhotoRv(FragmentPhotoBinding binding) {
        photoAdapter = new PhotoAdapter(R.layout.item_photo, new ArrayList<>());
        photoAdapter.addOnItemClickListener((position, item) -> {
            if (getContract() != null) {
                Bundle bundle = new Bundle();

                bundle.putParcelableArrayList(Constants.PHOTO_ITEMS, (ArrayList<PhotoModel>)createPhotoModels());
                bundle.putInt(Constants.PHOTO_CURRENT_ITEM, position);

                PhotoDetailsActivity.start(fragment, bundle, true);
            }
        });

        photoAdapter.setItems(createPhotoModels());

        binding.rvPhoto.swapAdapter(photoAdapter, true);
        photoAdapter.notifyDataSetChanged();
    }

    private List<File> getSnapshots() {
        String folderName = App.getContext().getApplicationContext().getPackageName();

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
        if (dir.exists()) {
            List<File> listFiles = Arrays.asList(dir.listFiles(file ->
                file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg")
            ));
            Collections.sort(listFiles, (File t, File t1) ->
                    t.lastModified() > t1.lastModified() ? -1 : t.lastModified() < t1.lastModified() ? 1 : 0);

            return listFiles;
        }
        return new ArrayList<> ();
    }

}
