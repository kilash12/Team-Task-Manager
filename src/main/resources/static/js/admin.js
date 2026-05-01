console.log("Admin.js loaded");

document.addEventListener("DOMContentLoaded", () => {
    const imageInput = document.querySelector("#image_file_input");
    const imagePreview = document.querySelector("#upload_image_preview");

    // Check if elements exist on the current page before adding listeners
    if (imageInput && imagePreview) {
        imageInput.addEventListener("change", function (event) {
            const file = event.target.files[0];

            // Ensure the uploaded file is actually an image
            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();

                reader.onload = function () {
                    imagePreview.setAttribute("src", reader.result);
                    imagePreview.classList.remove("hidden"); // Show image if hidden
                };

                reader.readAsDataURL(file);
            }
        });
    }
});