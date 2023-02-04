package com.elad.chatimeapp.screens.profile;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.elad.chatimeapp.BuildConfig;
import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentProfileBinding;
import com.elad.chatimeapp.dialogs.ImagePickerDialogFragment;
import com.elad.chatimeapp.dialogs.PermissionDialogFragment;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.utils.PermissionsUtil;
import com.elad.chatimeapp.utils.Util;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class ProfileFragment extends Fragment {
    private final static String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private NavController navController;
    private final static int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private final static int IMAGE_COMPRESSION = 80;
    private Uri imageUri;
    private final ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // permission granted, open camera
                    launchCamera();
                } else {
                    // permission not granted, request permission with rationale check
                    requestPermissionsWithRationaleCheck(true);
                }
            }
    );
    private final ActivityResultLauncher<String[]> storagePermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            isGranted -> {
                if (PermissionsUtil.checkIfAllPermissionsGranted(isGranted)) {
                    launchGallery();
                } else {
                    requestPermissionsWithRationaleCheck(false);
                }
            }
    );
    private final ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    cropImage(uri);
                }
            }
    );
    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    if (success) {
                        cropImage(imageUri);
                    }
                }
            }
    );
    private final ActivityResultLauncher<Intent> cropActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null)
                        handleUCropResult(result.getData());
                }
            }
    );

    private void requestPermissionsWithRationaleCheck(boolean isCamera) {
        if (isCamera) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), CAMERA)) {
                showRationaleDialog(true);
            } else {
                PermissionsUtil.openPermissionsSettings(requireActivity());
            }
        } else {
            // storage
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), WRITE_EXTERNAL_STORAGE)) {
                showRationaleDialog(false);
            } else {
                PermissionsUtil.openPermissionsSettings(requireActivity());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setModel(viewModel.getUser());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initViews();
        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                viewModel.updateUser(user);
            }
        });
    }

    private void initViews() {
        binding.profileLay.setOnClickListener(v -> showImagePickerOptions());
        binding.profileImg.setOnClickListener(v -> showImagePickerOptions());
        binding.profileBtnNext.setOnClickListener(v -> onNextClicked());
    }

    private void onNextClicked() {
        Editable name = binding.profileEdtName.getText();
        Editable gender = binding.profileEdtGender.getText();
        Editable status = binding.profileEdtStatus.getText();
        if (name == null || name.toString().isEmpty()) {
            binding.profileEdtName.setError(getString(R.string.name_required));
        } else if (gender == null || gender.toString().isEmpty()) {
            binding.profileEdtGender.setError(getString(R.string.gender_required));
        } else if (status == null || status.toString().isEmpty()) {
            binding.profileEdtStatus.setError(getString(R.string.status_required));
        } else {
            viewModel.getUser().setName(name.toString());
            viewModel.getUser().setGender(gender.toString());
            viewModel.getUser().setStatus(status.toString());

            viewModel.saveUser();

            navController.navigate(R.id.action_profile_dest_to_main_dest);
        }
    }

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), queryName(requireContext().getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.primaryColor));
        options.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primaryColor));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(requireContext(), R.color.primaryColor));

        options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);
        options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);


        UCrop uCrop = UCrop.of(sourceUri, destinationUri).withOptions(options);

        cropActivityResultLauncher.launch(uCrop.getIntent(requireContext()));
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private void launchCamera() {
        String fileName = System.currentTimeMillis() + ".jpg";
        File photoFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        imageUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        cameraLauncher.launch(imageUri);
    }

    private void launchGallery() {
        selectPictureLauncher.launch("image/*");
    }

    private void showRationaleDialog(boolean isCamera) {
        new PermissionDialogFragment(
                isCamera ? getString(R.string.camera_permission_title) : getString(R.string.storage_permissions_title),
                isCamera ? getString(R.string.camera_permission_message) : getString(R.string.storage_permissions_message),
                new PermissionDialogFragment.PermissionDialogCallback() {
                    @Override
                    public void onConfirm() {
                        if (isCamera)
                            PermissionsUtil.requestCameraPermission(cameraPermissionLauncher);
                        else // read external
                            PermissionsUtil.requestStoragePermissions(storagePermissionsLauncher);
                    }

                    @Override
                    public void onCancel() {
                        PermissionsUtil.openPermissionsSettings(requireActivity());
                    }
                }
        ).show(getChildFragmentManager(), PermissionDialogFragment.TAG);
    }

    public void showImagePickerOptions() {
        new ImagePickerDialogFragment(new ImagePickerDialogFragment.ImagePickerDialogCallback() {
            @Override
            public void onCamera() {
                if (!PermissionsUtil.hasCameraPermission(requireContext())) {
                    PermissionsUtil.requestCameraPermission(cameraPermissionLauncher);
                } else {
                    launchCamera();
                }
            }

            @Override
            public void onGallery() {
                if (!PermissionsUtil.hasStoragePermissions(requireContext())) {
                    PermissionsUtil.requestStoragePermissions(storagePermissionsLauncher);
                } else {
                    launchGallery();
                }
            }
        }).show(getChildFragmentManager(), ImagePickerDialogFragment.TAG);
    }

    private void handleUCropResult(Intent data) {
        try {
            final Uri resultUri = UCrop.getOutput(data);
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), resultUri);
            viewModel.getUser().setProfileImage(Util.encodeBitmapToBase64(bitmap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}