# Titan Camera

TitanCamera is Camera library for Android platform. It is build upon Camera and Camera2 API, and handles the selection of Camera API automatically based on SDK version. It solves most of the common issues faced by developer while developing in-app camera, such as Stretching of image etc. 



## Getting Started

This project contains **titan-camera** library and a tester app. To use the app you need to download the zip file or clone the repository and run the project in Android Studio. You will find MainActivity.java class in tester app where the demo code is written to help you to understand how to use **titan-camera** for your project.



### Demo
| Camera Preview      | Picture Taken      | 
|------------|-------------| 
| ![Camera Preview](https://github.com/vinodghai/titan-camera/blob/master/1_n.png?raw=true "Camera Preview") | ![Picture Taken](https://github.com/vinodghai/titan-camera/blob/master/2_n.png?raw=true "Picture Taken") |
  



### Camera API
* [Camera](https://developer.android.com/guide/topics/media/camera) - API used on devices running below Lollipop (SDK 21)
* [Camera2](https://developer.android.com/reference/android/hardware/camera2/package-summary.html) - API used on and above Lollipop (SDK 21)



### Features
* Easily integrateable in-app camera
* Fast and reliable
* Handles preview sizing, preview stretching, FPS rate, bitmap handing, out-of-memory errors, etc.
* Automatic selection of camera API to use
* Automatic handles if device does not have any or front camera.
* Take high-quality images
* Well tested
* Light weight



### Installation

1. Download the library.

2. Import in your project. Example in Android Stuio.

```
 Go to:  File / New / Import Module
  
  Select the path of the library and press ok to import. Android Studio will automaticall imports and adds the module in your project.
```


3. Create Camera object through CameraRequestBuilder to start camera. You can start camera from Activity instance, Fragment instance, or    context instance.

```
new CameraRequestBuilder()
                .setEnableSwitchCamera(true) //enable switch button to switch between front and back camera
                .setLensFacing(CameraProperties.LENS_FACING_BACK) //Camera to start (Front or back)
                .setCustomLayoutId(null) //Any layout that goes on top of the preview
                .build() // returns camera object
                .startCamera(context, this::onCapture) // starts camera
```



4. Take the result in custom callback (If you have passed so).

```
 @Override
    public void onCapture(@NonNull File file) {
        Toast.makeText(this, "Image received", Toast.LENGTH_SHORT).show();
    }
```


5. Take the result in onActivityResult.

```
  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK
                && requestCode == CameraRequestBuilder.REQUEST_CODE_CAMERA
                && data != null) {
            File file = (File) data.getSerializableExtra(CameraRequestBuilder.RESULT_IMAGE_FILE);
            if (file != null) {
                Toast.makeText(this, "Image received", Toast.LENGTH_SHORT).show();
            }
        }
    }
```


**Note: You can have the result image file in *CaptureResult (Callback)* or *onActivityResult*. You can receive result in Activity's *onActivityResult* as well as Fragment's *OnActivityResult*. You should consider getting results in *onActivityResult* if you want to do a FragmentTransaction after receving File.**


### CameraRequestBuilder

| Method       | Descritpion |
| ------------- | ------------- |
| setLensFacing | Sets the camera to be used. Example, CameraProperties.LENS_FACING_BACK  |
| setEnableSwitchCamera | Used to allow user to switch between front and back camera  |
| setCustomLayoutId | Used to plalce and placeholder layout on the top of camera preview  |
| setRootPath | Sets the root path where captured image will be saved. Default Picture directory of external storage |
| setImageFileName | Sets the name of the campture file  |
| build | returns the Camera object used to start Camera  |



### Camera

| Method       | Descritpion |
| ----------- | ------------- |
| startCamera | Takes context and CaptureResutlt callback where you will receive captured image  |
| startCamera | Takes Fragment instance and sends result in Activity's onActivityResultlt  |
| startCamera | Takes Activity instance and sends result in Activity's onActivityResultlt  |
| startCamera | Takes Fragment instance and Bundle that you will receive back in OnActivityResult. Bundle parameter is for you to get                   back any local parameter that you want to retrieve back in onActivityResult and donot want to create instance variable |
| startCamera | Takes Activity instance and Bundle that you will receive back in OnActivityResult. Bundle parameter is for you to get                   back any local parameter that you want to retrieve back in onActivityResult and donot want to create instance variable |



### Version

**1.0**



### Libraries used

* [PhotoView](https://github.com/chrisbanes/PhotoView) - To display the captured image with sliding feature.




### Author
**Vinod Kumar**
