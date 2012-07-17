var pictureSource;   // Picture source
var destinationType; // Sets the format of returned value 

// Wait for PhoneGap to connect with the device
document.addEventListener("deviceready", onDeviceReady, false);

// PhoneGap is ready to be used!
function onDeviceReady() {
    pictureSource = navigator.camera.PictureSourceType;
    destinationType = navigator.camera.DestinationType;
}

// Called when a photo is successfully retrieved
function onPhotoDataSuccess(imageData) {
    var capturedPhoto = document.getElementById('capturedPhoto');
    capturedPhoto.style.display = 'block';
    capturedPhoto.src = imageData;
    navigator.notification.alert("Picture Successfully Captured!");
}

// Called when a photo is successfully retrieved
function onPhotoURISuccess(imageURI) {
    var selectedFromPhotoLibrary = document.getElementById('selectedFromPhotoLibrary');
    selectedFromPhotoLibrary.style.display = 'block';
    selectedFromPhotoLibrary.src = imageURI;
    navigator.notification.alert("Picture Imported Captured!");
}

function capturePhoto() {
    // Take picture using device camera and retrieve image
    navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 50 });
}

// A button will call this function
//
function getPhoto(source) {
    // Retrieve image file location from specified source
    navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 50,
        destinationType: destinationType.FILE_URI,
        sourceType: source
    });
}
// Error Handling
function onFail(message) {
    alert('Failed because: ' + message);
}