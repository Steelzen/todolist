// Show the modal
window.handleCreateAccount = () => {
    console.log("Clicked create account");

    const modal  = document.getElementById("create-account-modal");

    modal.classList.remove("hidden");

    // Add click listener for the close button
    const closeModalButton = document.getElementById("closeModalButton");
    closeModalButton.addEventListener("click", () => {
        modal.classList.add("hidden");
    });
};

function handleCreateAccountSubmit() {
    console.log("Submit");
    const password = document.getElementById("create-account-password").value;
    const confirmPassword = document.getElementById("create-account-passwordConfirm").value;

    if(password !== confirmPassword) {
        alert('Passwords do not match');
        return false;
    }

    const modal  = document.getElementById("create-account-modal");

    modal.classList.add("hidden");

    return true;
}

function handleLogin() {
    console.log("Log in");
}