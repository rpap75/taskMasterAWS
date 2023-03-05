package com.rpap.taskmaster.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.task;
import com.amplifyframework.datastore.generated.model.taskTeam;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.rpap.taskmaster.R;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddATaskActivity";
    private String s3ImageKey = "";
    private String taskLocationKey = "";
    Spinner taskStatusSpinner;
    Spinner taskTeamSpinner;

    CompletableFuture<List<taskTeam>> taskTeamFuture = new CompletableFuture<>();

    ArrayList<String> teamNames;
    ArrayList<taskTeam> taskTeamArrayList;

    ActivityResultLauncher<Intent> activityResultLauncher;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        activityResultLauncher = getImagePickingActivityResultLauncher();
        taskStatusSpinner = findViewById(R.id.taskStatusSpinner);
        taskTeamSpinner = findViewById(R.id.addTaskActivityTeamSpinner);
        teamNames = new ArrayList<>();
        taskTeamArrayList = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(taskTeam.class),
                success -> {
                    Log.i(TAG, "Read taskTeam successfully!");
                    for (taskTeam databaseTaskTeam : success.getData()) {
                        teamNames.add(databaseTaskTeam.getName());
                        taskTeamArrayList.add(databaseTaskTeam);
                    }
                    taskTeamFuture.complete(taskTeamArrayList);
                    runOnUiThread(this::setUpSpinners);
                },
                failure -> {
                    taskTeamFuture.complete(null);
                    Log.e(TAG, "FAILED to read taskTeam" + failure);
                }
        );
        setUpSpinners();
        setUpSaveButton();
        setUpAddImageButton();
        getLocationsUpdates();
//                getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Log.e(TAG, "Location Callback Was Null");
            }
            assert location != null;
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            Log.i(TAG, "Out Latitude " + currentLatitude);
            Log.i(TAG, "Out Latitude " + currentLongitude);
        });

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            if (location == null) {
                Log.e(TAG, "Location Callback Was Null");
            }
//            assert location != null;
            assert location != null;
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            Log.i(TAG, "Out Latitude " + currentLatitude);
            Log.i(TAG, "Out Latitude " + currentLongitude);
        });
    }

    private void getLocationsUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 1000);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);


        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                try {
                    String address = geocoder.getFromLocation(
                                    Objects.requireNonNull(locationResult.getLastLocation()).getLatitude(),
                                    locationResult.getLastLocation().getLongitude(),
                                    1)
                            .get(0)
                            .getAddressLine(0);
                    taskLocationKey = address;
                    Log.i(TAG, "Repeating Current Location is: " + address);

                } catch (IOException ioe) {
                    Log.i(TAG, "Could Not Get Location: " + ioe);
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    public void setUpSpinners() {
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStatusEnum.values()
        ));
        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }

    public void setUpSaveButton() {
        findViewById(R.id.addTaskActivityAddTaskButton).setOnClickListener(v -> saveTask());
    }

    public void saveTask() {
        String selectedTaskTeamStringName = taskTeamSpinner.getSelectedItem().toString();
        try {
            taskTeamArrayList = (ArrayList<taskTeam>) taskTeamFuture.get();
        } catch (InterruptedException | ExecutionException ie) {
            ie.printStackTrace();
        }

        taskTeam selectedTeam = taskTeamArrayList.stream().filter(team -> team.getName().equals(selectedTaskTeamStringName)).findAny().orElseThrow(RuntimeException::new);

        task newTask = task.builder()
                .title(((EditText) findViewById(R.id.addTaskActivityTaskTitleInput)).getText().toString())
                .body(((EditText) findViewById(R.id.addTaskActivityTaskDescriptionInput)).getText().toString())
                .status((TaskStatusEnum) taskStatusSpinner.getSelectedItem())
                .taskTeam(selectedTeam)
                .s3ImageKey(s3ImageKey)
                .taskLocation(taskLocationKey)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                success -> Log.i(TAG, "Made A Task Successfully"),
                failure -> Log.e(TAG, "FAILED To Make A Task", failure)
        );

        Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
    }

    public void setUpAddImageButton() {
        findViewById(R.id.addTaskImagePicker).setOnClickListener(v -> launchImageSelectionIntent());
    }

    public void launchImageSelectionIntent() {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    assert result.getData() != null;
                    Uri pickedImageFileUri = result.getData().getData();

                    try {
                        InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                        String pickedImageFileName = getFileNameFromUri(pickedImageFileUri);
                        Log.i(TAG, "Successfully Got The Image: " + pickedImageFileName);
                        uploadInputStreamToS3(pickedImageInputStream, pickedImageFileName, pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could Not Get File From Picker!" + fnfe);
                    }
                }
        );
    }

    public void uploadInputStreamToS3(InputStream pickedImageInputStream, String
            pickedImageFileName, Uri pickedImageFileUri) {
        Amplify.Storage.uploadInputStream(
                pickedImageFileName,
                pickedImageInputStream,
                success -> {
                    Log.i(TAG, "SUCCESS! Uploaded File To S3! Filename is: " + success.getKey());
                    s3ImageKey = pickedImageFileName;
                    ImageView taskImageView = findViewById(R.id.addTaskImagePicker);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could Not Get File Stream From URI! " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure -> Log.e(TAG, "Failed To Upload File To S3 with Filename: " + pickedImageFileName + " with error: " + failure)
        );
    }

    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}