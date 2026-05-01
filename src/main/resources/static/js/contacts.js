console.log("Contacts.js loaded");

// Dynamic Base URL (Works perfectly on both Localhost and Live Server)
const baseURL = window.location.origin;

// Flowbite Modal Setup (Safe check)
const viewContactModalElement = document.getElementById("view_contact_modal");
let contactModal = null;

if (viewContactModalElement && typeof Modal !== 'undefined') {
    const options = {
        placement: "center",
        backdrop: "dynamic",
        backdropClasses: "bg-gray-900/80 backdrop-blur-sm fixed inset-0 z-40 transition-opacity",
        closable: true,
        onHide: () => console.log("Modal closed"),
        onShow: () => console.log("Modal opened")
    };
    contactModal = new Modal(viewContactModalElement, options);
}

// Open and Close helper functions
function openContactModal() {
    if (contactModal) contactModal.show();
}

function closeContactModal() {
    if (contactModal) contactModal.hide();
}

// Fetch Contact Data via API
async function loadContactdata(id) {
    if (!id) return;

    try {
        const response = await fetch(`${baseURL}/api/contacts/${id}`);
        if (!response.ok) throw new Error("Network response was not ok");

        const data = await response.json();
        console.log("Fetched Data:", data);

        // Helper function for safe DOM updates
        const setText = (selector, text) => {
            const el = document.querySelector(selector);
            if (el) el.textContent = text || "N/A";
        };

        setText("#contact_name", data.name);
        setText("#contact_email", data.email);
        setText("#contact_address", data.address);
        setText("#contact_phone", data.phoneNumber);
        setText("#contact_about", data.description);

        // Set Image
        const imgEl = document.querySelector("#contact_image");
        if (imgEl) imgEl.src = data.picture || '/images/logo.png';

        // Set Favorite Stars
        const favEl = document.querySelector("#contact_favorite");
        if (favEl) {
            if (data.favorite) {
                favEl.innerHTML = `
                    <div class="flex text-yellow-400 gap-1 text-lg">
                        <i class='fas fa-star'></i><i class='fas fa-star'></i><i class='fas fa-star'></i><i class='fas fa-star'></i><i class='fas fa-star'></i>
                    </div>`;
            } else {
                favEl.innerHTML = "<span class='text-gray-500 font-medium'>Not a Favorite Contact</span>";
            }
        }

        // Set Links
        const setLink = (selector, url, fallback) => {
            const el = document.querySelector(selector);
            if (el) {
                if (url) {
                    el.href = url;
                    el.textContent = url;
                    el.classList.remove('text-gray-500', 'cursor-default');
                    el.classList.add('text-blue-600', 'dark:text-blue-400', 'hover:underline');
                } else {
                    el.removeAttribute('href');
                    el.textContent = fallback;
                    el.classList.remove('text-blue-600', 'dark:text-blue-400', 'hover:underline');
                    el.classList.add('text-gray-500', 'cursor-default');
                }
            }
        };

        setLink("#contact_website", data.websiteLink, "No Website Provided");
        setLink("#contact_linkedIn", data.linkedInLink, "No LinkedIn Provided");

        openContactModal();

    } catch (error) {
        console.error("Error loading contact:", error);
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Something went wrong while fetching contact details!',
            background: document.documentElement.classList.contains('dark') ? '#1f2937' : '#ffffff',
            color: document.documentElement.classList.contains('dark') ? '#ffffff' : '#111827'
        });
    }
}

// Delete Contact with SweetAlert2
function deleteContact(id) {
    const isDark = document.documentElement.classList.contains('dark');

    Swal.fire({
        title: "Delete Contact?",
        text: "You won't be able to revert this action!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#dc2626", // Red
        cancelButtonColor: "#4b5563", // Gray
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "Cancel",
        background: isDark ? '#1f2937' : '#ffffff',
        color: isDark ? '#ffffff' : '#111827'
    }).then((result) => {
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contacts/delete/${id}`;
            window.location.replace(url);
        }
    });
}